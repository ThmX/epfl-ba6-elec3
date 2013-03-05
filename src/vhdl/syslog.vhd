-- ---------------------------------------------------------------------------
-- --- syslog.vhd
-- ---------------------------------------------------------------------------
-- --- Project	: LabSimulator - Package Library
-- --- Authors	: Thomas Denoréaz - thomas.denoreaz@epfl.ch
-- ---
-- --- Versions	:
-- --- 		- 2012.08.21 - Initial version
-- --- 		- 2012.08.30 - Added Simulator functions
-- ---------------------------------------------------------------------------

library ieee;
use ieee.std_logic_1164.all;
use ieee.std_logic_arith.all;
use ieee.std_logic_unsigned.all;
use ieee.std_logic_textio.all;
use std.textio.all;

use work.txt_util.all;

package syslog is
	
	-- --- Simulator -----
	procedure shrink_line(L : inout LINE; pos : in integer);
	
	procedure skip_white(L : inout LINE);
	
	procedure read_string(L: inout LINE; str: inout LINE);
	
	-- --- Testbenches -----
	type testbenches_message_t is (update_m, final_m, unknown_m);
	
	function str(msg: testbenches_message_t) return string;

	procedure print_message(msg_type: testbenches_message_t; msg: string);
	
end syslog;

package body syslog is
	
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
		variable pos: integer;
	begin
		pos := L'low;
		while pos <= L'high loop
			case L(pos) is
				when ' ' | HT  => pos := pos + 1;
				when others => exit;
			end case;
		end loop;
		shrink_line(L, pos);
	end;
	
	procedure read_string(L: inout LINE; str: inout LINE) is
		variable pos: integer;
	begin
		skip_white(L);
		pos := L'low;

		while pos <= L'high loop
			case L(pos) is
				when ' ' | HT => exit;
				when others => pos := pos + 1;
			end case;
		end loop;
		
		str := new string'(L(L'low to pos-1));
		shrink_line(L, pos+1);
	end;
	

-- ---------------------------------------------------------------------------
-- --- Testbenches Functions -------------------------------------------------
-- ---------------------------------------------------------------------------
	
	function str(msg: testbenches_message_t) return string is
	begin
		case msg is
			when update_m	=> return "Update";
			when final_m	=> return "Final";
			when unknown_m	=> return "Unknown";
		end case;
	end function;

	procedure print_message(msg_type: testbenches_message_t; msg: string) is
	begin
		print("# ** " & str(msg_type) & ": " & msg);
		print("     Time: " & time'image(now));
	end print_message;
	
end syslog;

-- ---------------------------------------------------------------------------
-- ----- End of file ---------------------------------------------------------
-- ---------------------------------------------------------------------------