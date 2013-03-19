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
-- ---------------------------------------------------------------------------

library ieee;
use ieee.std_logic_1164.all;

use work.utils.all;

-- Electronic Control Unit
entity ecu is
	port(
		clk       : in  ubit;
		rst       : in  ubit;

		op        : in  ubits(3 downto 0);
		opx       : in  ubits(3 downto 0);
		value_in  : in  uword;

		value_out : out uword
	);
end entity ecu;

architecture RTL of ecu is
	constant ECU_CLOCK  : ubits(3 downto 0) := X"0";
	constant ECU_CHRONO : ubits(3 downto 0) := X"1";

	signal clk_ms : ubit;

	signal clock_cs        : ubit;
	signal clock_timestamp : uword;

	signal chrono_cs        : ubit;
	signal chrono_timestamp : uword;
begin
	clk_ms <= clk;

	-- Clock
	ecu_clock_0 : entity work.ecu_clock
		port map(
			clk       => clk_ms,
			rst       => rst,
			cs        => clock_cs,
			load      => opx(0),
			value     => value_in,
			timestamp => clock_timestamp
		);

	-- Chrono
	ecu_chrono_0 : entity work.ecu_chrono
		port map(
			clk        => clk_ms,
			rst        => rst,
			cs         => chrono_cs,
			start_stop => opx(0),
			clear      => opx(1),
			timestamp  => chrono_timestamp
		);

	process(clk, rst) is
	begin
		if rising_edge(clk) then
			if rst = '1' then
			--
			else
				clock_cs  <= '0';
				chrono_cs <= '0';

				case op is
					when ECU_CLOCK =>
						clock_cs <= '1';

					when ECU_CHRONO =>
						chrono_cs <= '1';

					when others => null;
				end case;

			end if;
		end if;
	end process;

end architecture RTL;

-- ---------------------------------------------------------------------------
-- ----- End of file ---------------------------------------------------------
-- ---------------------------------------------------------------------------