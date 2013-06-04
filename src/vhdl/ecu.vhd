-- ---------------------------------------------------------------------------
-- --- ecu.vhd
-- ---------------------------------------------------------------------------
-- --- Project	: BA6 - Elec III
-- --- Authors	:
-- ---		(183785) Thomas Denoréaz
-- ---		(204393) Johan Berdat
-- ---		(194875) Alexandre Carlessi
-- ---
-- --- Versions	:
-- --- 		- 2013.03.19 - Initial version
-- --- 		- 2013.06.01 - Measure system
-- ---------------------------------------------------------------------------

library ieee;
use ieee.std_logic_1164.all;
use ieee.numeric_std.all;

use work.utils.all;

-- Electronic Control Unit
entity ecu is
	port(
		clk       : in  ubit;
		rst       : in  ubit;

		-- MMU
		mmu_rw    : out ubit;
		mmu_addr  : out ubits(3 downto 0);
		mmu_in    : in  udword;
		mmu_out   : out udword;

		-- HCI
		op        : in  ubits(3 downto 0);
		opx       : in  ubits(3 downto 0);

		velocity  : out udword;
		engine    : out udword;
		distance  : out udword;
		timestamp : out udword
	);
end entity ecu;

architecture RTL of ecu is
	constant CONST_WAIT_TICK : uword := X"0000";

	constant ECU_CLOCK  : ubits(3 downto 0) := X"0";
	constant ECU_CHRONO : ubits(3 downto 0) := X"1";

	constant ADDR_VELOCITY  : ubits(3 downto 0) := X"0";
	constant ADDR_ENGINE    : ubits(3 downto 0) := X"1";
	constant ADDR_TIMESTAMP : ubits(3 downto 0) := X"8";
	constant ADDR_DISTANCE  : ubits(3 downto 0) := X"9";

	signal clk_ms : ubit;

	signal clock_cs        : ubit;
	signal clock_timestamp : udword;

	signal chrono_cs        : ubit;
	signal chrono_timestamp : udword;

	type state_t is (INIT, LOAD, TIMER, FETCH, COMPUTE);
	signal state : state_t;

	signal counter : uword;

	type sensor_t is (S_FIRST, S_VELOCITY, S_ENGINE, S_LAST);
	signal sensor : sensor_t;

	signal int_velocity : udword;
	signal int_engine   : udword;

	type measure_t is (M_FIRST, M_TIMESTAMP, M_DISTANCE, M_LAST);
	signal measure : measure_t;

	signal delta_t       : udword;
	signal mem_timestamp : udword;
	signal mem_distance  : udword;

begin
	clk_ms <= clk;

	-- Clock
	ecu_clock_0 : entity work.ecu_clock(RTL)
		port map(
			clk       => clk_ms,
			rst       => rst,
			cs        => clock_cs,
			load      => opx(0),
			value     => mmu_in,
			timestamp => clock_timestamp
		);

	-- Chrono
	ecu_chrono_0 : entity work.ecu_chrono(RTL)
		port map(
			clk        => clk_ms,
			rst        => rst,
			cs         => chrono_cs,
			start_stop => opx(0),
			clear      => opx(1),
			timestamp  => chrono_timestamp
		);

	UI : process(clk, rst) is
	begin
		if rst = '1' then
			clock_cs  <= '0';
			chrono_cs <= '0';
			timestamp <= (others => '0');

		elsif rising_edge(clk) then
			clock_cs  <= '0';
			chrono_cs <= '0';

			case op is
				when ECU_CLOCK =>
					clock_cs  <= '1';
					timestamp <= clock_timestamp;

				when ECU_CHRONO =>
					chrono_cs <= '1';
					timestamp <= chrono_timestamp;

				when others => null;
			end case;

		end if;
	end process UI;

	velocity <= int_velocity;
	engine   <= int_engine;
	distance <= mem_distance;

	controller : process(clk, rst) is
		variable mult : uddword;
	begin
		if rst = '1' then
			mmu_rw   <= '0';
			mmu_addr <= (others => '0');
			mmu_out  <= (others => '0');

			counter <= (others => '0');

			int_velocity <= (others => '0');
			int_engine   <= (others => '0');

			delta_t       <= (others => '0');
			mem_timestamp <= (others => '0');
			mem_distance  <= (others => '0');

			state   <= INIT;
			sensor  <= S_FIRST;
			measure <= M_FIRST;

		elsif rising_edge(clk) then
			mmu_addr <= (others => '0');
			mmu_rw   <= '0';

			counter <= counter + 1;

			case state is
				when INIT =>
					counter <= (others => '0');
					sensor  <= S_FIRST;
					measure <= M_FIRST;
					state   <= LOAD;

				when LOAD =>
					case measure is
						when M_FIRST =>
							measure  <= M_TIMESTAMP;
							mmu_addr <= ADDR_TIMESTAMP;

						when M_TIMESTAMP =>
							mem_timestamp <= mmu_in;

							measure  <= M_DISTANCE;
							mmu_addr <= ADDR_DISTANCE;

						when M_DISTANCE =>
							mem_distance <= mmu_in;

							measure <= M_LAST;

						when M_LAST =>
							measure <= M_FIRST;
							state   <= TIMER;

					end case;

				when TIMER =>
					if counter > CONST_WAIT_TICK then -- wait a moment
						counter <= (others => '0');
						sensor  <= S_FIRST;
						measure <= M_FIRST;
						state   <= FETCH;
					end if;

				when FETCH =>
					case sensor is
						when S_FIRST =>
							sensor   <= S_VELOCITY;
							mmu_addr <= ADDR_VELOCITY;
						when S_VELOCITY =>
							int_velocity <= mmu_in;

							sensor   <= S_ENGINE;
							mmu_addr <= ADDR_ENGINE;

						when S_ENGINE =>
							int_engine <= mmu_in;

							sensor <= S_LAST;

						when S_LAST =>
							sensor <= S_FIRST;
							state  <= COMPUTE;
					end case;

				when COMPUTE =>
					case measure is
						when M_FIRST =>
							measure <= M_TIMESTAMP;

						when M_TIMESTAMP =>
							delta_t  <= clock_timestamp - mem_timestamp;
							mmu_addr <= ADDR_TIMESTAMP;
							mmu_rw   <= '1';
							mmu_out  <= clock_timestamp;

							measure <= M_DISTANCE;

						when M_DISTANCE =>
							mult         := delta_t * int_velocity;
							mem_distance <= mem_distance + mult(37 downto 6);

							mmu_addr <= ADDR_DISTANCE;
							mmu_rw   <= '1';
							mmu_out  <= mem_distance + mult(37 downto 6);
							measure  <= M_LAST;

						when M_LAST =>
							measure <= M_FIRST;
							state   <= TIMER;

					end case;
			end case;
		end if;
	end process controller;
end architecture RTL;

-- ---------------------------------------------------------------------------
-- ----- End of file ---------------------------------------------------------
-- ---------------------------------------------------------------------------