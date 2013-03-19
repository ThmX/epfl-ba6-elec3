-- ---------------------------------------------------------------------------
-- --- utils.vhd
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
use ieee.numeric_std.all;

package utils is
	subtype ubits is unsigned;
	subtype ubit is std_logic;

	subtype uword is ubits(31 downto 0);

	function is_set(b : ubit) return boolean;
	function are_set(b : ubits) return boolean;

	function is_clear(b : ubit) return boolean;
	function are_clear(b : ubits) return boolean;

end utils;

package body utils is
	function is_set(b : ubit) return boolean is
	begin
		if b = '1' then
			return true;
		else
			return false;
		end if;
	end function is_set;

	function are_set(b : ubits) return boolean is
	begin
		if b = (b'range => '1') then
			return true;
		else
			return false;
		end if;
	end function are_set;

	function is_clear(b : ubit) return boolean is
	begin
		if b = '0' then
			return true;
		else
			return false;
		end if;
	end function is_clear;

	function are_clear(b : ubits) return boolean is
	begin
		if b = (b'range => '0') then
			return true;
		else
			return false;
		end if;
	end function are_clear;

end utils;

-- ---------------------------------------------------------------------------
-- ----- End of file ---------------------------------------------------------
-- ---------------------------------------------------------------------------