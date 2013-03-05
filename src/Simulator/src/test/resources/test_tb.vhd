-- ---------------------------------------------------------------------------
-- --- test_tb.vhd
-- ---------------------------------------------------------------------------
-- --- Project	: LabSimulator
-- --- Authors	: Thomas Denoréaz - thomas.denoreaz@epfl.ch
-- ---
-- --- Versions	:
-- --- 		- 2012.08.22 - Initial version
-- ---------------------------------------------------------------------------

library ieee;
use ieee.std_logic_1164.all;
use std.textio.all;

entity test_tb is end;

architecture testbench of test_tb is
	
	constant CLK_PERIOD : time := 20 ns;
	
	signal clk			: std_logic := '0';
	signal finished		: boolean := false;

begin
	-- Clock generator
	clk <= not clk after CLK_PERIOD / 2 when not finished;
	
	-- Final report
	process
		variable l : line;
	begin
		wait for 4 * CLK_PERIOD;
		finished <= true;
		
		write (l, String'("# ** Final: Simulation ended successfully."));
		writeline (output, l);
		wait;
		
		wait;
	end process;
	
end testbench;

-- ---------------------------------------------------------------------------
-- ----- End of file ---------------------------------------------------------
-- ---------------------------------------------------------------------------