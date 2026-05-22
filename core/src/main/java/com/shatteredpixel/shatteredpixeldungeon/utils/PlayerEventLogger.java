/*
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2024 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package com.shatteredpixel.shatteredpixeldungeon.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import java.time.Instant;

/** Player-facing gameplay events (death, potions, floor changes, item use, etc.). */
public final class PlayerEventLogger {

	private static final String LOG_TAG = "PLAYER_EVENT";
	private static final String LOG_PATH = "logs/player-events.log";

	private PlayerEventLogger() {}

	public static synchronized void info(String source, String event, String details) {
		write("INFO", source, event, details);
	}

	/** Hero HP change: Increase_or_decrease=+ or -, quantity=amount, cause=reason. */
	public static void playerBlood( boolean increase, int quantity, String cause ) {
		if ( quantity <= 0 ) {
			return;
		}
		String sign = increase ? "+" : "-";
		String safeCause = cause == null || cause.isEmpty() ? "unknown" : cause;
		info( "STATE", "PLAYER_BLOOD",
				"Increase_or_decrease=" + sign + ",quantity=" + quantity + ",cause=" + safeCause );
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
