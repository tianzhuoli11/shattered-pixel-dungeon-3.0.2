package com.shatteredpixel.shatteredpixeldungeon.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.utils.ScreenUtils;
import com.watabou.utils.DeviceCompat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public final class ScreenshotSaver {

	private ScreenshotSaver() {}

	/**
	 * Captures the current OpenGL framebuffer and writes a PNG next to the user's Desktop folder.
	 * Desktop only; no-op elsewhere. Triggered from {@link com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene}
	 * via Shift+P (same idea as Alt+Enter fullscreen: not a user-rebindable keybinding).
	 */
	public static void captureToDesktopFolder() {
		if (!DeviceCompat.isDesktop()) {
			return;
		}
		int w = Gdx.graphics.getBackBufferWidth();
		int h = Gdx.graphics.getBackBufferHeight();
		Pixmap pixmap = null;
		try {
			pixmap = ScreenUtils.getFrameBufferPixmap(0, 0, w, h);
			pixmap = flipVertically(pixmap);
			String ts = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
			FileHandle dir = Gdx.files.absolute(
					System.getProperty("user.home") + "/Desktop/ShatteredPD-Screenshots");
			dir.mkdirs();
			FileHandle out = dir.child("SPD_" + ts + ".png");
			PixmapIO.writePNG(out, pixmap);
			Gdx.app.log("SPD-Screenshot", "Saved: " + out.file().getAbsolutePath());
		} catch (Throwable t) {
			Gdx.app.error("SPD-Screenshot", "Failed to save screenshot", t);
		} finally {
			if (pixmap != null) {
				pixmap.dispose();
			}
		}
	}

	/** OpenGL framebuffer rows are bottom-up; PNG expects top-down. */
	private static Pixmap flipVertically(Pixmap src) {
		int w = src.getWidth();
		int h = src.getHeight();
		Pixmap dst = new Pixmap(w, h, src.getFormat());
		dst.drawPixmap(src, 0, 0, w, h, 0, h, w, -h);
		src.dispose();
		return dst;
	}
}
