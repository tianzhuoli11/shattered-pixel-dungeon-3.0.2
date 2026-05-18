package com.shatteredpixel.shatteredpixeldungeon.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public final class PlayerEventLogger {

	private static final String LOG_TAG = "PLAYER_EVENT";
	/** Legacy aggregate log (all sessions append). */
	private static final String AGGREGATE_LOG_PATH = "logs/player-events.log";
	/** One .txt file per play session (new game or continue). */
	private static final String SESSION_DIR = "logs/sessions";

	private static final DateTimeFormatter SESSION_FILE_TS =
			DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss").withZone(ZoneId.systemDefault());

	private static FileHandle sessionFile;

	private PlayerEventLogger() {}

	/**
	 * Starts a new session log file, e.g. {@code logs/sessions/session_20260518_150430.txt}.
	 * Call when a new run begins or when loading a save from the title screen.
	 */
	public static synchronized void startNewSession() {
		if (Gdx.files == null) return;
		try {
			FileHandle dir = Gdx.files.local(SESSION_DIR);
			dir.mkdirs();
			String name = "session_" + SESSION_FILE_TS.format(Instant.now()) + ".txt";
			sessionFile = dir.child(name);
			sessionFile.writeString(
					Instant.now() + " | INFO | PlayerEventLogger | SESSION_START | file=" + name + "\n",
					false, "UTF-8");
			if (Gdx.app != null) {
				Gdx.app.log(LOG_TAG, "Session log file: " + sessionFile.file().getAbsolutePath());
			}
		} catch (Throwable ignored) {
			sessionFile = null;
		}
	}

	public static synchronized void info(String source, String event, String details) {
		write("INFO", source, event, details);
	}

	private static void write(String level, String source, String event, String details) {
		String line = Instant.now().toString() + " | " + level + " | " + source + " | " + event + " | " + details;
		if (Gdx.app != null) {
			Gdx.app.log(LOG_TAG, line);
		}
		if (Gdx.files == null) return;
		try {
			if (sessionFile != null) {
				sessionFile.writeString(line + "\n", true, "UTF-8");
			}
			FileHandle aggregate = Gdx.files.local(AGGREGATE_LOG_PATH);
			aggregate.parent().mkdirs();
			aggregate.writeString(line + "\n", true, "UTF-8");
		} catch (Throwable ignored) {
			// Logging should never break gameplay flow.
		}
	}
}
