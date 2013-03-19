-- ---------------------------------------------------------------------------
-- --- horloge.vhd
-- ---------------------------------------------------------------------------
-- --- Project	: BA6 - Elec III
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

use work.utils.all;

entity ecu_clock is
	port(
		clk       : in  ubit;
		rst       : in  ubit;

		cs        : in  ubit;
		load      : in  ubit;
		value     : in  uword;

		timestamp : out uword
	);
end entity ecu_clock;

architecture RTL of ecu_clock is
	signal timestamp_intern : uword;
begin
	timestamp <= timestamp_intern;

	process(clk, rst) is
	begin
		if rising_edge(clk) then
			if rst = '1' then
				timestamp_intern <= (others => '0');
			else
				timestamp_intern <= timestamp_intern + 1;
				if is_set(cs) and is_set(load) then
					timestamp_intern <= value;
				end if;
			end if;
		end if;
	end process;

end architecture RTL;

-- ---------------------------------------------------------------------------
-- ----- End of file ---------------------------------------------------------
-- ---------------------------------------------------------------------------