/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2024 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.shatteredpixel.shatteredpixeldungeon.utils;

import com.badlogic.gdx.Gdx;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;

import java.util.Locale;

/**
 * Periodic in-game performance samples for ISO 25010 "performance efficiency" style analysis.
 * Logs FPS together with depth and mob counts so traces can be compared (sparse vs crowded).
 */
public final class PerformanceSampler {

	/** Seconds between log lines; 2s is enough for video demos across load levels without flooding the console. */
	// private static final float SAMPLE_INTERVAL_SEC = 5f;
	private static final float SAMPLE_INTERVAL_SEC = 2f;

	private static final int HEAVY_MOBS_TOTAL = 14;
	private static final int HEAVY_MOBS_FOV = 5;

	private static float accumDt;
	private static int framesInWindow;
	private static float timeUntilNextSample;

	private PerformanceSampler() {}

	public static void onGameFrame() {
		if (Dungeon.hero == null || Dungeon.level == null) return;

		float dt = Gdx.graphics.getDeltaTime();
		dt = Math.min(Math.max(dt, 1e-4f), 0.25f);
		accumDt += dt;
		framesInWindow++;
		timeUntilNextSample += dt;
		if (timeUntilNextSample < SAMPLE_INTERVAL_SEC) return;
		timeUntilNextSample -= SAMPLE_INTERVAL_SEC;

		float fpsAvg = accumDt > 0 ? framesInWindow / accumDt : 0f;
		accumDt = 0f;
		framesInWindow = 0;

		int mobsAlive = 0;
		int mobsInFov = 0;
		for (Mob m : Dungeon.level.mobs) {
			if (!m.isAlive()) continue;
			mobsAlive++;
			if (Dungeon.level.heroFOV[m.pos]) {
				mobsInFov++;
			}
		}

		String load;
		if (mobsAlive == 0) {
			load = "empty";
		} else if (mobsAlive >= HEAVY_MOBS_TOTAL || mobsInFov >= HEAVY_MOBS_FOV) {
			load = "heavy";
		} else {
			load = "normal";
		}

		int fpsGdx = Gdx.graphics.getFramesPerSecond();
		String details = String.format(Locale.US,
				"fps_avg=%.1f fps_gdx=%d depth=%d branch=%d mobs_alive=%d mobs_in_fov=%d load=%s",
				fpsAvg, fpsGdx, Dungeon.depth, Dungeon.branch, mobsAlive, mobsInFov, load);
		PlayerEventLogger.perf("PerformanceSampler", "PERF_SAMPLE", details);
	}
}

