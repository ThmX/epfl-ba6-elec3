-- ------------------------------------------------------------------
-- --- chrono.vhd
-- ------------------------------------------------------------------
-- --- Project	: BA6 - Elec III
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
use ieee.numeric_std.all;

use work.utils.all;

entity ecu_chrono is
	port(
		clk        : in  ubit;
		rst        : in  ubit;

		cs         : in  ubit;
		start_stop : in  ubit;
		clear      : in  ubit;

		timestamp  : out udword
	);
end entity ecu_chrono;

architecture RTL of ecu_chrono is
	signal timestamp_intern : udword;
	signal run              : ubit;
begin
	timestamp <= timestamp_intern;

	process(clk, rst) is
	begin
		if rst = '1' then
			timestamp_intern <= (others => '0');
			run              <= '0';
		elsif rising_edge(clk) then
			if is_set(run) then
				timestamp_intern <= timestamp_intern + 1;
			end if;

			if is_set(cs) then
				if is_set(clear) then
					timestamp_intern <= (others => '0');
				elsif is_set(start_stop) then
					run <= not run;
				end if;
			end if;
		end if;
	end process;

end architecture RTL;