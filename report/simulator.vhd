-- ------------------------------------------------------------------
-- --- clock_simulator.vhd
-- ------------------------------------------------------------------
-- --- Project	: LabSimulator - Package Library
-- --- Authors	:
-- ---		(183785) Thomas Denoréaz
-- ---		(204393) Johan Berdat
-- ---		(194875) Alexandre Carlessi
-- ---
-- --- Versions	:
-- --- 		- 2013.03.19 - Initial version
-- ------------------------------------------------------------------

library ieee;
use ieee.std_logic_1164.all;
use ieee.std_logic_textio.all;
use ieee.numeric_std.all;
use std.textio.all;

use work.txt_util.all;
use work.vunit.all;
use work.utils.all;

entity ecu_simulator is
end;

architecture simulator of ecu_simulator is

	-- ----- Simulated circuit --------------------------------------

	signal clk       : ubit              := '0';
	signal rst       : ubit              := '1';
	signal mmu_rw    : ubit              := '0';
	signal mmu_addr  : ubits(3 downto 0) := (others => '0');
	signal mmu_in    : udword            := (others => '0');
	signal mmu_out   : udword            := (others => '0');
	signal op        : ubits(3 downto 0) := (others => '0');
	signal opx       : ubits(3 downto 0) := (others => '0');
	signal velocity  : udword            := (others => '0');
	signal engine    : udword            := (others => '0');
	signal distance  : udword            := (others => '0');
	signal timestamp : udword            := (others => '0');

	signal sensor_velocity  : udword := (others => '0');
	signal sensor_engine    : udword := (others => '0');
	signal sensor_timestamp : udword := (others => '0');

	-- ----- Simulated value ------------------------------------------
	signal sim_accelerate : ubit := '0';
	signal sim_brake      : ubit := '0';

begin

	-- ----------------------------------------------------------------
	-- ----- Simulated circuit ----------------------------------------
	-- ----------------------------------------------------------------

	-- Electronic Control Unit

	ecu_inst : entity work.ecu
		port map(clk       => clk,
			     rst       => rst,
			     mmu_rw    => mmu_rw,
			     mmu_addr  => mmu_addr,
			     mmu_in    => mmu_in,
			     mmu_out   => mmu_out,
			     op        => op,
			     opx       => opx,
			     velocity  => velocity,
			     engine    => engine,
			     distance  => distance,
			     timestamp => timestamp);

	-- ----------------------------------------------------------------
	-- ----- Memory implementation ------------------------------------
	-- ----------------------------------------------------------------
	mmu_read : process(mmu_addr, sensor_velocity, sensor_engine, sensor_timestamp) is
	begin
		case mmu_addr is
			when X"0"   => mmu_in <= sensor_velocity;
			when X"1"   => mmu_in <= sensor_engine;
			when X"8"   => mmu_in <= sensor_timestamp;
			when others => mmu_in <= (others => '0');
		end case;
	end process mmu_read;

	mmu_write : process(clk, rst) is
	begin
		if rst = '1' then
			sensor_velocity  <= (others => '0');
			sensor_engine    <= (others => '0');
			sensor_timestamp <= (others => '0');

		elsif rising_edge(clk) then
			if sim_brake = '1' then
				if sensor_velocity > 10 then
					sensor_velocity <= sensor_velocity - 5;
				else
					sensor_velocity <= (others => '0');
				end if;
			elsif sim_accelerate = '1' then
				sensor_velocity <= sensor_velocity + 1;
				sensor_engine <= sensor_engine + ('0' & sensor_velocity(31 downto 1));
			end if;

			if mmu_rw = '1' then
				case mmu_addr is
					when X"8"   => sensor_timestamp <= mmu_out;
					when others => null;
				end case;
			end if;
		end if;
	end process mmu_write;

	-- --------------------------------------------------------------
	-- ----- Simulator implementation -------------------------------
	-- --------------------------------------------------------------

	rst <= '0' after 1 ns;

	-- Print all updated signals
	process is
	begin
		vunit_update(str(timestamp) & " " & str(mmu_rw) & " " & str(mmu_addr) & " " & str(mmu_out) & " " & str(velocity) & " " & str(engine) & " " & str(distance));
		wait on timestamp, mmu_rw, mmu_addr, mmu_out, velocity, engine, distance;
	end process;
end simulator;