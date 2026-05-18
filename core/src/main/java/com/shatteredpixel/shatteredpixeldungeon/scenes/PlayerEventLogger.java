package com.shatteredpixel.shatteredpixeldungeon.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import java.time.Instant;

public final class PlayerEventLogger {

	private static final String LOG_TAG = "PLAYER_EVENT";
	private static final String LOG_PATH = "logs/player-events.log";

	private PlayerEventLogger() {}

	public static synchronized void info(String source, String event, String details) {
		write("INFO", source, event, details);
	}

	private static void write(String level, String source, String event, String details) {
		String line = Instant.now().toString() + " | " + level + " | " + source + " | " + event + " | " + details;
		if (Gdx.app != null) {
			Gdx.app.log(LOG_TAG, line);
		}
		if (Gdx.files != null) {
			try {
				FileHandle file = Gdx.files.local(LOG_PATH);
				file.parent().mkdirs();
				file.writeString(line + "\n", true, "UTF-8");
			} catch (Throwable ignored) {
				// Logging should never break gameplay flow.
			}
		}
	}
}
