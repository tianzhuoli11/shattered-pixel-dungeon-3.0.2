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
import java.nio.ByteBuffer;

public final class ScreenshotSaver {

	private ScreenshotSaver() {}

	/**
	 * Captures the current OpenGL framebuffer and writes a PNG next to the user's Desktop folder.
	 * Desktop only; no-op elsewhere. Triggered from {@link com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene}
	 * via Shift+H (same idea as Alt+Enter fullscreen: not a user-rebindable keybinding).
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
		
        
        if (src.getFormat() == Pixmap.Format.RGBA8888) {
			int rowBytes = w * 4;
			ByteBuffer from = src.getPixels();
			ByteBuffer to = dst.getPixels();
			byte[] line = new byte[rowBytes];
			for (int y = 0; y < h; y++) {
				from.position(y * rowBytes);
				from.get(line, 0, rowBytes);
				to.position((h - 1 - y) * rowBytes);
				to.put(line, 0, rowBytes);
			}
			from.rewind();
			to.rewind();
		} else {
			for (int y = 0; y < h; y++) {
				for (int x = 0; x < w; x++) {
					dst.drawPixel(x, h - 1 - y, src.getPixel(x, y));
				}
			}
		}

		src.dispose();
		return dst;
	}
}
