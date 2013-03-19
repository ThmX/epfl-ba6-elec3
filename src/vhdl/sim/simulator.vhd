-- ---------------------------------------------------------------------------
-- --- clock_simulator.vhd
-- ---------------------------------------------------------------------------
-- --- Project	: LabSimulator - Package Library
-- --- Authors	:
-- ---		(183785) Thomas Denoréaz
-- ---		(204393) Johan Berdat
-- ---		(194875) Alexandre Carlessi
-- ---
-- --- Versions	:
-- --- 		- 2013.03.19 - Initial version
-- ---------------------------------------------------------------------------

library ieee;
use ieee.std_logic_1164.all;
use ieee.std_logic_textio.all;
use std.textio.all;

use work.txt_util.all;
use work.vunit.all;
use work.utils.all;

entity lab4_clock_simulator is
end;

architecture simulator of lab4_clock_simulator is

	-- ----- Simulated circuit -----------------------------------------------

	signal clk       : ubit              := '0';
	signal rst       : ubit              := '1';
	signal op        : ubits(3 downto 0) := (others => '0');
	signal opx       : ubits(3 downto 0) := (others => '0');
	signal value_in  : uword             := (others => '0');
	
	signal value_out : uword             := (others => '0');

-- -----------------------------------------------------------------------
begin

	-- -----------------------------------------------------------------------
	-- ----- Simulated circuit -----------------------------------------------
	-- -----------------------------------------------------------------------

	-- Electronic Control Unit
	ecu_0 : entity work.ecu
		port map(
			clk       => clk,
			rst       => rst,
			op        => op,
			opx       => opx,
			value_in  => value_in,
			value_out => value_out
		);

	-- -----------------------------------------------------------------------
	-- ----- Simulator implementation ----------------------------------------
	-- -----------------------------------------------------------------------

	rst <= '0' after 1 ns;

	process
	begin
		vunit_update(str(value_out));
		wait on value_out;
	end process;

end simulator;

-- ---------------------------------------------------------------------------
-- ----- End of file ---------------------------------------------------------
-- ---------------------------------------------------------------------------