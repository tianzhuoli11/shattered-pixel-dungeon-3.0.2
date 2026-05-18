package com.shatteredpixel.shatteredpixeldungeon.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.watabou.utils.DeviceCompat;

import java.io.File;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public final class PlayerEventLogger {

	private static final String LOG_TAG = "PLAYER_EVENT";

	private static final DateTimeFormatter SESSION_FILE_TS =
			DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss").withZone(ZoneId.systemDefault());

	private static FileHandle logsRoot;
	private static FileHandle sessionFile;
	/** Print resolved log folder once so teammates can find files regardless of IDE working directory. */
	private static boolean loggedDiskPathOnce;

	private PlayerEventLogger() {}

	/**
	 * Desktop: {@code <project>/desktop/logs} (resolved from user.dir).<br>
	 * Other platforms: LibGDX {@code Gdx.files.local("logs/")}.
	 */
	private static FileHandle logsRoot() {
		if (logsRoot != null) return logsRoot;
		if (Gdx.files == null) return null;
		if (DeviceCompat.isDesktop()) {
			File dir = findDesktopLogsDirectory();
			dir.mkdirs();
			logsRoot = Gdx.files.absolute(dir.getAbsolutePath());
		} else {
			logsRoot = Gdx.files.local("logs");
			logsRoot.mkdirs();
		}
		return logsRoot;
	}

	private static File findDesktopLogsDirectory() {
		File start = new File(System.getProperty("user.dir")).getAbsoluteFile();
		File parent = start.getParentFile();
		if ("desktop".equals(start.getName()) && parent != null && new File(parent, "core").isDirectory()) {
			return new File(start, "logs");
		}
		File dir = start;
		for (int i = 0; i < 14 && dir != null; i++) {
			if (new File(dir, "desktop").isDirectory() && new File(dir, "core").isDirectory()) {
				return new File(dir, "desktop/logs");
			}
			dir = dir.getParentFile();
		}
		return new File(start, "logs");
	}

	/**
	 * Starts a new session log file under {@code logs/sessions/session_yyyyMMdd_HHmmss.txt}.
	 * Call when a new run begins or when loading a save from the title screen.
	 */
	public static synchronized void startNewSession() {
		FileHandle root = logsRoot();
		if (root == null) return;
		try {
			FileHandle sessions = root.child("sessions");
			sessions.mkdirs();
			String name = "session_" + SESSION_FILE_TS.format(Instant.now()) + ".txt";
			sessionFile = sessions.child(name);
			sessionFile.writeString(
					Instant.now() + " | INFO | PlayerEventLogger | SESSION_START | file=" + name + "\n",
					false, "UTF-8");
			String absRoot = root.file().getAbsolutePath();
			root.child("LOGS_README.txt").writeString(
					"Player event logs are written here.\n"
							+ "Aggregate: player-events.log\n"
							+ "Per session: sessions/session_*.txt\n"
							+ "Folder: " + absRoot + "\n",
					false, "UTF-8");
			if (Gdx.app != null) {
				Gdx.app.log(LOG_TAG, "Logs folder: " + absRoot);
				Gdx.app.log(LOG_TAG, "Session log file: " + sessionFile.file().getAbsolutePath());
			}
		} catch (Throwable t) {
			sessionFile = null;
			if (Gdx.app != null) {
				Gdx.app.error(LOG_TAG, "startNewSession failed", t);
			}
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
		FileHandle root = logsRoot();
		if (root == null) {
			if (Gdx.app != null && !loggedDiskPathOnce) {
				loggedDiskPathOnce = true;
				Gdx.app.error(LOG_TAG, "logsRoot() is null (Gdx.files not ready?) — disk log disabled");
			}
			return;
		}
		if (Gdx.app != null && !loggedDiskPathOnce) {
			loggedDiskPathOnce = true;
			Gdx.app.log(LOG_TAG, "Disk logs folder: " + root.file().getAbsolutePath());
		}
		try {
			if (sessionFile != null) {
				sessionFile.writeString(line + "\n", true, "UTF-8");
			}
			FileHandle aggregate = root.child("player-events.log");
			aggregate.writeString(line + "\n", true, "UTF-8");
		} catch (Throwable t) {
			if (Gdx.app != null) {
				Gdx.app.error(LOG_TAG, "Failed to write player log line", t);
			}
		}
	}
}
