
/*
	To get key events on windows and unix, I used this topic:
	http://cboard.cprogramming.com/c-programming/63166-kbhit-linux.html
	
	See ANSI escape codes for output formatting (UNIX):
	http://en.wikipedia.org/wiki/ANSI_escape_code
	
	On windows, see these pages to erase lines and change colors:
	http://support.microsoft.com/kb/99261
	http://msdn.microsoft.com/en-us/library/ms686047%28VS.85%29.aspx
	
	Gregorian calendar starts on the 15th october 1582:
	http://en.wikipedia.org/wiki/Gregorian_calendar
	http://en.wikipedia.org/wiki/Leap_year
*/


#include <stdio.h>
#include <time.h>


/*
	INPUT/OUTPUT MANAGEMENT
*/

#ifdef WIN32
#include <conio.h>
#include <windows.h>
#else
#include <termios.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/time.h>
#endif

#ifdef WIN32

void initKeyboard(void) {}

void killKeyboard(void) {}

#else

void changemode(int dir) {
	static struct termios oldt, newt;
	if (dir) {
		tcgetattr(STDIN_FILENO, &oldt);
		newt = oldt;
		newt.c_lflag &= ~(ICANON | ECHO);
		tcsetattr(STDIN_FILENO, TCSANOW, &newt);
	}
	else
		tcsetattr(STDIN_FILENO, TCSANOW, &oldt);
}

void initKeyboard(void) {
	changemode(1);
}

void killKeyboard(void) {
	changemode(0);
}

int kbhit(void) {
	struct timeval tv;
	fd_set rdfs;
	tv.tv_sec = 0;
	tv.tv_usec = 0;
	FD_ZERO(&rdfs);
	FD_SET(STDIN_FILENO, &rdfs);
	select(STDIN_FILENO + 1, &rdfs, NULL, NULL, &tv);
	return FD_ISSET(STDIN_FILENO, &rdfs);
}

#endif

int getKey(void) {
	if (!kbhit())
		return -1;
#ifdef WIN32
	return getch();
#else
	return getchar();
#endif
}

// Get a mask according to current pressed arrow keys. Returns -1 if q was pressed.
#define IN_UP    1
#define IN_DOWN  2
#define IN_RIGHT 4
#define IN_LEFT  8
//#define USE_ARROWS // sequenced keys do not work well on UNIX.
#ifndef USE_ARROWS
#define KEY_UP    'w'
#define KEY_DOWN  's'
#define KEY_RIGHT 'd'
#define KEY_LEFT  'a'
#endif
int getInput(void) {
	int input = 0;
#ifdef USE_ARROWS
	int ch;
	int expect = 0;
	while (1) {
		ch = getKey();
#ifdef WIN32
		if (expect) {
			switch (ch) {
			case 72: input |= IN_UP; break;
			case 75: input |= IN_LEFT; break;
			case 77: input |= IN_RIGHT; break;
			case 80: input |= IN_DOWN;
			}
			expect = 0;
		}
		else {
			switch (ch) {
			case -1 : return input;
			case 'q': return -1;
			case 224: expect = 1;
			}
		}
#else
		switch (expect) {
		case 2:
			switch (ch) {
			case 'A': input |= IN_UP; break;
			case 'D': input |= IN_LEFT; break;
			case 'C': input |= IN_RIGHT; break;
			case 'B': input |= IN_DOWN;
			}
			expect = 0;
			break;
		case 1:
			expect = ch == '[' ? 2 : 0;
			break;
		case 0:
			switch (ch) {
			case -1 : return input;
			case 'q': return -1;
			case 27 : expect = 1;
			}
		}
#endif
	}
#else
	while (1) {
		switch (getKey()) {
		case KEY_UP   : input |= IN_UP; break;
		case KEY_LEFT : input |= IN_LEFT; break;
		case KEY_RIGHT: input |= IN_RIGHT; break;
		case KEY_DOWN : input |= IN_DOWN; break;
		case -1       : return input;
		case 'q'      : return -1;
		}
	}
#endif
}

// Sleep specified amount of milliseconds.
void wait(int milli) {
#ifdef WIN32
	Sleep(milli);
#else
	usleep(milli * 1000);
#endif
}

// Change output color (0 = white, 1 = red)
void setOutputColor(int color) {
#ifdef WIN32
	HANDLE hConsole = GetStdHandle(STD_OUTPUT_HANDLE);
	SetConsoleTextAttribute(hConsole, color ? FOREGROUND_RED : FOREGROUND_RED | FOREGROUND_GREEN | FOREGROUND_BLUE);
#else
	printf(color ? "\x1b[31m" : "\x1b[0m");
#endif
}

// Erase specified amount of lines, starting from current location. No guarantee if current position is not at the end.
void clearLastLines(int count) {
#ifdef WIN32
	HANDLE hConsole;
	CONSOLE_SCREEN_BUFFER_INFO csbi;
	DWORD cCharsWritten;
	hConsole = GetStdHandle(STD_OUTPUT_HANDLE);
	GetConsoleScreenBufferInfo(hConsole, &csbi);
	csbi.dwCursorPosition.X = 0;
	csbi.dwCursorPosition.Y -= count;
	FillConsoleOutputCharacter(hConsole, (TCHAR)' ', csbi.dwSize.X * count, csbi.dwCursorPosition, &cCharsWritten);
	SetConsoleCursorPosition(hConsole, csbi.dwCursorPosition);
#else
	printf("\033[%dF\033[J", count);
#endif
}


/*
	TIME MANAGEMENT TOOLS
*/

// Get whether y is a leap year.
int isLeapYear(int y) {
	return y % 4 == 0 && !(y % 100 == 0 && y % 400 != 0);
}

// Get how many leap years are between 1582 and y (included) (->leapYears(2012) = 105).
int leapYears(int y) {
	return (y - 1580) / 4 - (y - 1500) / 100 + (y - 1200) / 400;
}

// Get how many days are between 15.10.1582 and 01.01.y (-> adaysAtYear(1582) = 0, adaysAtYear(1583) = 365, ...)
long adaysAtYear(int y) {
	return (y - 1582l) * 365 + leapYears(y - 1) - 287;
}

// Get year according to adays (->yearsAtAdays(800) = 2)
int yearAtAdays(long ad) {
	int y = 1582;
	while (adaysAtYear(y + 1) < ad)
		++y;
	return y;
}

// Get how many days are between 1. jan and 1. m.
int ydaysAtMonth(int m, int leap) {
	int yd = 0;
	switch (m) {
		case 12: yd += 30;
		case 11: yd += 31;
		case 10: yd += 30;
		case 9 : yd += 31;
		case 8 : yd += 31;
		case 7 : yd += 30;
		case 6 : yd += 31;
		case 5 : yd += 30;
		case 4 : yd += 31;
		case 3 : yd += 28 + leap;
		case 2 : yd += 31;
	}
	return yd;
}

// Get month according to ydays (->monthAtYdays(31) = 1).
int monthAtYdays(int yd, int leap) {
	int m = 1;
	while (ydaysAtMonth(m + 1, leap) <= yd)
		++m;
	return m;
}

// Convert a date to adays.
long toAdays(int y, int m, int d) {
	return adaysAtYear(y) + ydaysAtMonth(m, isLeapYear(y)) + d - 1;
}

// Convert adays to a date.
void fromAdays(long ad, int * y, int * m, int * d) {
	int leap, yd;
	*y = yearAtAdays(ad);
	leap = isLeapYear(*y);
	yd = (int)(ad - adaysAtYear(*y));
	*m = monthAtYdays(yd, leap);
	*d = yd - ydaysAtMonth(*m, leap) + 1;
}

// Convert a time to milliseconds.
long toMilli(int h, int m, int s, int ms) {
	return ms + 1000l * (s + 60l * (m + 60l * h));
}

// Convert milliseconds to a time.
void fromMilli(long mi, int * h, int * m, int * s, int * ms) {
	*ms = (int)(mi % 1000);
	mi /= 1000;
	*s = (int)(mi % 60);
	mi /= 60;
	*m = (int)(mi % 60);
	*h = (int)(mi / 60);
}

#define MILLI_PER_DAY (1000l * 60 * 60 * 24)


/*
	TIME MANAGEMENT
*/

void fromDate(long long date, int * year, int * month, int * day, int * hour, int * min, int * sec, int * milli) {
	fromAdays((long)(date / MILLI_PER_DAY), year, month, day);
	fromMilli((long)(date % MILLI_PER_DAY), hour, min, sec, milli);
}

long long toDate(int year, int month, int day, int hour, int min, int sec, int milli) {
	return MILLI_PER_DAY * (long long)toAdays(year, month, day) + toMilli(hour, min, sec, milli);
}

long long addMilli(long long date, int millis) {
	return date + millis;
}

long long addSec(long long date, int secs) {
	return date + (long long)secs * 1000;
}

long long addMin(long long date, int mins) {
	return date + (long long)mins * 1000 * 60;
}

long long addHour(long long date, int hours) {
	return date + (long long)hours * 1000 * 60 * 60;
}

long long addDay(long long date, int days) {
	return date + (long long)days * 1000 * 60 * 60 * 24;
}

long long addMonth(long long date, int months) {
	int y, m, d, buf;
	long ad, milli;
	ad = (long)(date / MILLI_PER_DAY);
	milli = (long)(date % MILLI_PER_DAY);
	fromAdays(ad, &y, &m, &d);
	m += months;
	if (m > 12) {
		buf = (m - 1) / 12;
		y += buf;
		m -= buf * 12;
	}
	else if (m <= 0) {
		buf = (-m) / 12 + 1;
		y -= buf;
		m += buf * 12;
	}
	return (long long)toAdays(y, m, d) * MILLI_PER_DAY + milli;
}

long long addYear(long long date, int years) {
	int y, m, d;
	long ad, milli;
	ad = (long)(date / MILLI_PER_DAY);
	milli = (long)(date % MILLI_PER_DAY);
	fromAdays(ad, &y, &m, &d);
	y += years;
	return (long long)toAdays(y, m, d) * MILLI_PER_DAY + milli;
}

long long (*addTime[])(long long, int) = {
	addMilli, addSec, addMin, addHour, addDay, addMonth, addYear
};

long long fromCTime(time_t t) {
	struct tm * info = localtime(&t);
	return toDate(info->tm_year + 1900, info->tm_mon + 1, info->tm_mday, info->tm_hour, info->tm_min, info->tm_sec, 0);
}


/*
	APPLICATION
*/

#define CLOCK_DELTA 200

#define MODE_SHOWTIME 0
#define MODE_SETTIME  1
#define MODE_CHRONO   2
int mode, submode;

long long now, newnow;

#define CHRONO_STOP  0
#define CHRONO_PAUSE 1
#define CHRONO_RUN   2
int chronomode;
long long chrono, oldchrono;

// Code executed at each clock tick. Delta is in milliseconds.
void tick(int input, int delta) {
	int year, mon, day, hour, min, sec, milli;
	int buf;
	long lday;
	static int notFirst = 0;
	// Update time
	now = addMilli(now, delta);
	newnow = addMilli(newnow, delta);
	if (chronomode != CHRONO_STOP)
		chrono += delta;
	// Handle input
	switch (mode) {
	case MODE_SHOWTIME:
		buf = !!(input & IN_RIGHT) - !!(input & IN_LEFT);
		switch (buf) {
		case -1:
			mode = MODE_SETTIME;
			submode = 0;
			break;
		case 1:
			mode = MODE_CHRONO;
		}
		break;
	case MODE_SETTIME:
		buf = !!(input & IN_UP) - !!(input & IN_DOWN);
		if (buf)
			newnow = addTime[submode](newnow, buf);
		submode += !!(input & IN_LEFT) - !!(input & IN_RIGHT);
		if (submode < 0) {
			newnow = now;
			mode = MODE_SHOWTIME;
		}
		else if (submode > 6) {
			now = newnow;
			mode = MODE_SHOWTIME;
		}
		break;
	case MODE_CHRONO:
		switch (input) {
			case IN_LEFT:
				mode = MODE_SHOWTIME;
				break;
			case IN_RIGHT:
				chrono = 0;
				break;
			case IN_UP:
				if (chronomode == CHRONO_PAUSE)
					chronomode = CHRONO_RUN;
				else if (chronomode == CHRONO_RUN)
					chronomode = CHRONO_PAUSE;
				oldchrono = chrono;
				break;
			case IN_DOWN:
				if (chronomode == CHRONO_STOP)
					chronomode = CHRONO_RUN;
				else
					chronomode = CHRONO_STOP;
		}
	}
	// Render
	if (notFirst)
		clearLastLines(6);
	else
		notFirst = 1;
	switch (mode) {
	case MODE_SHOWTIME:
		printf("< to change time\n"
		       "> to go to chrono\n"
		       "^\n"
			   "v\n\n");
		fromDate(now, &year, &mon, &day, &hour, &min, &sec, &milli);
		printf("%04d.%02d.%02d-%02d:%02d:%02d.%03d\n", year, mon, day, hour, min, sec, milli);
		break;
	case MODE_SETTIME:
		printf("< to move left (exit to validate)\n"
		       "> to move right (exit to cancel)\n"
		       "^ to increment\n"
			   "v to decrement\n\n");
		fromDate(newnow, &year, &mon, &day, &hour, &min, &sec, &milli);
		if (submode == 6) setOutputColor(1);
		printf("%04d", year);
		if (submode == 6) setOutputColor(0);
		printf(".");
		if (submode == 5) setOutputColor(1);
		printf("%02d", mon);
		if (submode == 5) setOutputColor(0);
		printf(".");
		if (submode == 4) setOutputColor(1);
		printf("%02d", day);
		if (submode == 4) setOutputColor(0);
		printf("-");
		if (submode == 3) setOutputColor(1);
		printf("%02d", hour);
		if (submode == 3) setOutputColor(0);
		printf(":");
		if (submode == 2) setOutputColor(1);
		printf("%02d", min);
		if (submode == 2) setOutputColor(0);
		printf(":");
		if (submode == 1) setOutputColor(1);
		printf("%02d", sec);
		if (submode == 1) setOutputColor(0);
		printf(".");
		if (submode == 0) setOutputColor(1);
		printf("%03d", milli);
		if (submode == 0) setOutputColor(0);
		printf("\n");
		break;
	case MODE_CHRONO:
		printf("< to show time\n"
		       "> to clear\n"
		       "^ to pause/resume (continues in background)\n"
			   "v to stop/start\n\n");
		if (chronomode == CHRONO_PAUSE) {
			setOutputColor(1);
			lday = (long)(oldchrono / MILLI_PER_DAY);
			fromMilli((long)(oldchrono % MILLI_PER_DAY), &hour, &min, &sec, &milli);
			printf("%ld:%02d:%02d:%02d.%03d\n", lday, hour, min, sec, milli);
			setOutputColor(0);
		}
		else {
			lday = (long)(chrono / MILLI_PER_DAY);
			fromMilli((long)(chrono % MILLI_PER_DAY), &hour, &min, &sec, &milli);
			printf("%ld:%02d:%02d:%02d.%03d\n", lday, hour, min, sec, milli);
		}
	}
}

int main(void) {
	int in;
	printf("\nUBER CLOCK - v1.0\nJojo le Barjos\n\n");
	printf("q to leave\n");
	initKeyboard();
	mode = MODE_SHOWTIME;
	submode = 0;
	newnow = now = fromCTime(time(NULL));
	chronomode = CHRONO_STOP;
	oldchrono = chrono = 0;
	while ((in = getInput()) >= 0) {
		tick(in, CLOCK_DELTA);
		wait(CLOCK_DELTA);
	}
	killKeyboard();
	return 0;
}
