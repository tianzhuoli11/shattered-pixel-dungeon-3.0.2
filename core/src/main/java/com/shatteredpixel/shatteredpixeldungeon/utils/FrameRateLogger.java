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

/** Performance / FPS samples — separate from {@link PlayerEventLogger} player events. */
public final class FrameRateLogger {

	private static final String LOG_TAG = "RECORD_FRAME_RATE";
	private static final String LOG_PATH = "logs/frame-rate.log";

	private FrameRateLogger() {}

	public static synchronized void sample(String details) {
		String line = Instant.now().toString() + " | INFO | PerformanceSampler | PERF_SAMPLE | " + details;
		if (Gdx.app != null) {
			Gdx.app.log(LOG_TAG, line);
		}
		if (Gdx.files != null) {
			try {
				FileHandle file = Gdx.files.local(LOG_PATH);
				file.parent().mkdirs();
				file.writeString(line + "\n", true, "UTF-8");
			} catch (Throwable ignored) {
			}
		}
	}
}
