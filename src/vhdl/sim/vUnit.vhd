-- ---------------------------------------------------------------------------
-- --- vUnit.vhd
-- ---------------------------------------------------------------------------
-- --- Project	: vUnit (VHDL Unit)
-- --- Authors	: Thomas Denoréaz - thomas.denoreaz@epfl.ch
-- ---
-- --- Versions	:
-- --- 		- 2012.08.21 - Initial version
-- --- 		- 2013.01.07 - Rename vUnit
-- ---------------------------------------------------------------------------

library ieee;
use ieee.std_logic_1164.all;
use ieee.std_logic_textio.all;
use ieee.numeric_std.all;
use std.textio.all;

use work.txt_util.all;

package vUnit is

	constant VERSION : String := "0.0.1";

	-- --- Testbenches -----
	type vunit_severity is (start_m, end_m, update_m, note_m, assert_m, fail_m);

	procedure vunit_start;
	procedure vunit_start(msg : string);

	procedure vunit_end;

	procedure vunit_update(msg : string);
	procedure vunit_note(msg : string);

	procedure vunit_assert(cond : boolean);
	procedure vunit_assert(cond : boolean; msg : string);

	procedure vunit_fail;
	procedure vunit_fail(msg : string);

	-- --- Simulator -----
	procedure shrink_line(L : inout LINE; pos : in integer);
	procedure skip_white(L : inout LINE);
	procedure read_string(L : inout LINE; str : inout LINE);

end vUnit;

package body vUnit is

	-- ---------------------------------------------------------------------------
	-- --- Testbenches Functions -------------------------------------------------
	-- ---------------------------------------------------------------------------

	function str(msg : vunit_severity) return string is
	begin
		case msg is
			when start_m  => return "Start";
			when end_m    => return "End";
			when update_m => return "Update";
			when note_m   => return "Note";
			when assert_m => return "Assert";
			when fail_m   => return "Fail";
		end case;
	end function;

	procedure vunit_print_message(msg_type : vunit_severity; msg : string) is
	begin
		print("# ** " & str(msg_type) & ":" & time'image(now) & ": " & msg);
	end vunit_print_message;

	procedure vunit_start(msg : string) is
	begin
		vunit_print_message(start_m, msg);
	end;

	procedure vunit_start is
	begin
		vunit_start("test");
	end;

	procedure vunit_end is
	begin
		vunit_print_message(end_m, "");
	end;

	procedure vunit_update(msg : string) is
	begin
		vunit_print_message(update_m, msg);
	end;

	procedure vunit_note(msg : string) is
	begin
		vunit_print_message(note_m, msg);
	end;

	procedure vunit_assert(cond : boolean; msg : string) is
	begin
		if not cond then
			vunit_print_message(assert_m, msg);
		end if;
	end;

	procedure vunit_assert(cond : boolean) is
	begin
		vunit_assert(cond, "Assert failed.");
	end;

	procedure vunit_fail is
	begin
		vunit_fail("Failed.");
	end;

	procedure vunit_fail(msg : string) is
	begin
		vunit_print_message(fail_m, msg);
	end;

	-- ---------------------------------------------------------------------------
	-- --- Simulator Functions ---------------------------------------------------
	-- ---------------------------------------------------------------------------

	-- Taken from std.textio
	procedure shrink_line(L : inout LINE; pos : in integer) is
		variable old_L : LINE := L;
	begin
		if pos > 1 and pos < L'high then
			L := new string'(old_L(pos to old_L'high));
			Deallocate(old_L);
		end if;
	end;

	-- Taken from std.textio
	procedure skip_white(L : inout LINE) is
		variable pos : integer;
	begin
		pos := L'low;
		while pos <= L'high loop
			case L(pos) is
				when ' ' | HT => pos := pos + 1;
				when others   => exit;
			end case;
		end loop;
		shrink_line(L, pos);
	end;

	procedure read_string(L : inout LINE; str : inout LINE) is
		variable pos : integer;
	begin
		skip_white(L);
		pos := L'low;

		while pos <= L'high loop
			case L(pos) is
				when ' ' | HT => exit;
				when others   => pos := pos + 1;
			end case;
		end loop;

		str := new string'(L(L'low to pos - 1));
		shrink_line(L, pos + 1);
	end;

end vUnit;

-- ---------------------------------------------------------------------------
-- ----- End of file ---------------------------------------------------------
-- ---------------------------------------------------------------------------