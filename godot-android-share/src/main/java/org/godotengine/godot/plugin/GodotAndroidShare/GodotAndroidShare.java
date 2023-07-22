package org.godotengine.godot.plugin.GodotAndroidShare;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;

import org.godotengine.godot.Godot;
import org.godotengine.godot.plugin.GodotPlugin;
import org.godotengine.godot.plugin.UsedByGodot;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/** @noinspection unused */
public class GodotAndroidShare extends GodotPlugin {

	private static final String TAG = "godot";
    private final Activity activity;

    public GodotAndroidShare(Godot godot) {
        super(godot);
        activity = godot.getActivity();
    }

    @UsedByGodot
    public void shareText(String title, String subject, String text) {
        Log.d(TAG, "shareText called");
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, text);
        activity.startActivity(Intent.createChooser(sharingIntent, title));
    }

    @UsedByGodot
    public void sharePic(String path, String title, String subject, String text) {
        Log.d(TAG, "sharePic called");

        File f = new File(path);

        Uri uri;
        try {
            uri = FileProvider.getUriForFile(activity, activity.getPackageName(), f);
        } catch (IllegalArgumentException e) {
            Log.e(TAG, "The selected file can't be shared: " + path);
            return;
        }

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/*");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        shareIntent.putExtra(Intent.EXTRA_TEXT, text);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        activity.startActivity(Intent.createChooser(shareIntent, title));
    }

    /** @noinspection deprecation */
    @NonNull
    @Override
    public List<String> getPluginMethods() {
        return Arrays.asList("sharePic", "shareText");
    }

    @NonNull
    @Override
    public String getPluginName() {
        return "GodotAndroidShare";
    }
}
