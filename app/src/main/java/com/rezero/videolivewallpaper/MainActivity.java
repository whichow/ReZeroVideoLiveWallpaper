package com.rezero.videolivewallpaper;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.Image;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class MainActivity extends Activity {

    final String PREFERENCES = "com.rezero.videolivewallpaper_preferences";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        LinearLayout layout = new LinearLayout(this);
//        layout.setGravity(Gravity.CENTER);
//        layout.setOrientation(LinearLayout.VERTICAL);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.main_activity);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title);
//        ScrollView scrollView = new ScrollView(this);
//        ScrollView.LayoutParams params = new ScrollView.LayoutParams(ScrollView.LayoutParams.MATCH_PARENT, ScrollView.LayoutParams.MATCH_PARENT);
//        scrollView.setLayoutParams(params);
//        scrollView.setFillViewport(true);
//
//        setContentView(scrollView);
//        layout.addView(scrollView);

//        TableLayout table = new TableLayout(this);
//        TableLayout.LayoutParams params1 = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT);
//        table.setLayoutParams(params1);
//
//        scrollView.addView(table);

        TableLayout table = (TableLayout)findViewById(R.id.image_table);
//
//        int rowNumber = 2;
//        int columnNumber = 3;
//        Random random = new Random();
//        for (int i=0; i < rowNumber; i++) {
//            TableRow row = new TableRow(MainActivity.this);
//            for (int j=0; j < columnNumber; j++) {
//                int value = random.nextInt(100) + 1;
//                Button tv = new Button(MainActivity.this);
//                TableRow.LayoutParams buttonParams = new TableRow.LayoutParams(0, 300);
//                buttonParams.weight = 1f;
//                tv.setLayoutParams(buttonParams);
//                tv.setText(String.valueOf(value));
//                row.addView(tv);
//            }
//            table.addView(row);
//        }

        try {
            final String[] list = getAssets().list("videos");
            Log.d("VideoLiveWallpaper", "list:" + list.length);

            int columnNum = 2;
            for (int i = 0; i < list.length; i += columnNum) {
                TableRow row = new TableRow(this);
//                TableLayout.LayoutParams rowParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.MATCH_PARENT);
//                rowParams.weight = 1;
//                row.setLayoutParams(rowParams);
                table.addView(row);
                for(int j = 0; j < columnNum; j++) {
                    final int index = i + j;
                    if(index < list.length) {
                        final ImageButton imageButton = new ImageButton(this);
                        TableRow.LayoutParams buttonParams = new TableRow.LayoutParams(0, 800);
                        buttonParams.weight = 1f;
                        imageButton.setLayoutParams(buttonParams);
                        imageButton.setBackgroundColor(Color.WHITE);
                        imageButton.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        imageButton.setPadding(0, 0, 0, 0);
//                        imageButton.setAdjustViewBounds(true);

                        final String videoFile = "videos/" + list[index];
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    final AssetFileDescriptor descriptor = getAssets().openFd(videoFile);
                                    MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                                    retriever.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
                                    final Bitmap bitmap = retriever.getFrameAtTime();
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            imageButton.setImageBitmap(bitmap);
                                        }
                                    });
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();

                        imageButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                SharedPreferences sharedPreferences = getSharedPreferences("LWP", Context.MODE_MULTI_PROCESS);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("videoFile", videoFile);
                                editor.apply();
                                VideoLiveWallpaper.setToWallPaper(MainActivity.this);
                            }
                        });
                        Log.d("VideoLiveWallpaper", list[index]);

                        row.addView(imageButton);
                    }
                    else {
                        ImageView image = new ImageView(this);
                        TableRow.LayoutParams buttonParams = new TableRow.LayoutParams(0, 800);
                        buttonParams.weight = 1f;
                        image.setLayoutParams(buttonParams);
                        image.setBackgroundColor(Color.WHITE);
                        image.setScaleType(ImageView.ScaleType.CENTER_CROP);
//                        image.setAdjustViewBounds(true);
                        row.addView(image);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

//        TableRow row = new TableRow(this);
//
//        table.addView(row);
//
//        ImageButton imageButton = new ImageButton(this);
//        TableRow.LayoutParams buttonParams = new TableRow.LayoutParams(0, 1000);
//        buttonParams.weight = 1f;
//        imageButton.setLayoutParams(buttonParams);
//
//        ImageButton imageButton1 = new ImageButton(this);
//        imageButton1.setLayoutParams(buttonParams);
//
//        row.addView(imageButton);
//        row.addView(imageButton1);
//
////        setContentView(R.layout.main_activity);
//        try {
//            final AssetFileDescriptor descriptor = getAssets().openFd("872d42371716db170693ddef39000c91.mp4");
//            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
//            retriever.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
//            Bitmap bitmap = retriever.getFrameAtTime();
////          Bitmap bitmap = ThumbnailUtils.createVideoThumbnail("file:///android_asset/872d42371716db170693ddef39000c91.mp4", MediaStore.Video.Thumbnails.MINI_KIND);
////            ImageButton imageButton = (ImageButton)findViewById(R.id.video_preview1);
//            imageButton.setImageBitmap(bitmap);
//            imageButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    VideoLiveWallpaper.setToWallPaper(MainActivity.this, descriptor);
//                }
//            });
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        try {
//            final AssetFileDescriptor descriptor = getAssets().openFd("d98f7d0b11eb282c630cab1fa1ff2353.mp4");
//            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
//            retriever.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
//            Bitmap bitmap = retriever.getFrameAtTime();
////          Bitmap bitmap = ThumbnailUtils.createVideoThumbnail("file:///android_asset/872d42371716db170693ddef39000c91.mp4", MediaStore.Video.Thumbnails.MINI_KIND);
////            ImageButton imageButton = (ImageButton)findViewById(R.id.video_preview2);
//            imageButton1.setImageBitmap(bitmap);
//            imageButton1.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    VideoLiveWallpaper.setToWallPaper(MainActivity.this, descriptor);
//                }
//            });
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

//        getPermission();
//        SharedPreferences updateReader = getSharedPreferences(PREFERENCES, MODE_PRIVATE);
//        String version_name = updateReader.getString(getString(R.string.version_name), " ");
//        assert version_name != null;
//        if (!version_name.equals(getString(R.string.version_name))) updateDialog();
//        Button btn1 = findViewById(R.id.choose_video_file);
//        btn1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View p1) {
//                chooseVideo();
//            }
//        });
//        Button btn2 = findViewById(R.id.add_video_file_path);
//        btn2.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View p1) {
//                final EditText edit = new EditText(MainActivity.this);
//                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
//                builder.setTitle(getString(R.string.add_path));
//                builder.setView(edit);
//                builder.setPositiveButton(getString(R.string.apply), new DialogInterface.OnClickListener() {
//                    @TargetApi(Build.VERSION_CODES.O)
//                    @Override
//                    public void onClick(DialogInterface p1, int p2) {
//                        String add_path = edit.getText().toString();
//                        copyFile(new File(add_path), new File(getFilesDir().toPath() + "/file.mp4"));
//                        VideoLiveWallpaper.setToWallPaper(MainActivity.this);
//                    }
//                });
//                builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface p1, int p2) {
//                    }
//                });
//                builder.setCancelable(true);
//                AlertDialog dialog = builder.create();
//                dialog.setCanceledOnTouchOutside(true);
//                dialog.show();
//            }
//        });
//        Button btn3 = findViewById(R.id.settings);
//        btn3.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View p1) {
//                Intent intent = new Intent(MainActivity.this, Settings.class);
//                startActivity(intent);
//            }
//        });
    }

    private void getPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
    }

    private void updateDialog() {
        final AlertDialog.Builder normalDialog = new AlertDialog.Builder(MainActivity.this);
        normalDialog.setTitle(getString(R.string.update_log) + "(" + getString(R.string.version_name) + ")");
        normalDialog.setMessage(getString(R.string.update_log_context));
        normalDialog.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        SharedPreferences.Editor editor = getSharedPreferences(PREFERENCES, MODE_PRIVATE).edit();
        editor.putString(getString(R.string.version_name), getString(R.string.version_name));
        editor.apply();
        normalDialog.show();
    }

    private void chooseVideo() {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (resultCode == Activity.RESULT_OK) {
//            Uri uri = data.getData();
//            assert uri != null;
//            if ("file".equalsIgnoreCase(uri.getScheme())) {
//                copyFile(new File(Objects.requireNonNull(uri.getPath())), new File(getFilesDir() + "/file.mp4"));
//                VideoLiveWallpaper.setToWallPaper(this);
//                return;
//            }
//            copyFile(new File(getPath(this, uri)), new File(getFilesDir() + "/file.mp4"));
//            VideoLiveWallpaper.setToWallPaper(this);
//        }

    }

    public void copyFile(File fromFile, File toFile) {
        FileInputStream fileInputStream = null;
        FileOutputStream fileOutputStream = null;
        FileChannel fileChannelInput = null;
        FileChannel fileChannelOutput = null;
        try {
            fileInputStream = new FileInputStream(fromFile);
            fileOutputStream = new FileOutputStream(toFile);
            fileChannelInput = fileInputStream.getChannel();
            fileChannelOutput = fileOutputStream.getChannel();
            fileChannelInput.transferTo(0, fileChannelInput.size(), fileChannelOutput);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileInputStream != null)
                    fileInputStream.close();
                if (fileChannelInput != null)
                    fileChannelInput.close();
                if (fileOutputStream != null)
                    fileOutputStream.close();
                if (fileChannelOutput != null)
                    fileChannelOutput.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressLint("NewApi")
    public String getPath(final Context context, final Uri uri) {
        if (DocumentsContract.isDocumentUri(context, uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.parseLong(id));
                return getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public String getDataColumn(Context context, Uri uri, String selection,
                                String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
}
