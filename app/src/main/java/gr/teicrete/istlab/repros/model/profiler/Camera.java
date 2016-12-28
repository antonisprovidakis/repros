package gr.teicrete.istlab.repros.model.profiler;

import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.jwetherell.motiondetection.detection.MotionStateChangeListener;
import com.jwetherell.motiondetection.detection.RgbMotionDetection;
import com.jwetherell.motiondetection.image.ImageProcessing;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Antonis on 26-Dec-16.
 */

public class Camera implements MobileSensor {

    public static int PICTURE_DELAY = 200; // ms

    private SurfaceView preview = null;
    private SurfaceHolder previewHolder = null;
    private android.hardware.Camera camera = null;
    private boolean inPreview = false;
    private RgbMotionDetection detector = null;
    private volatile AtomicBoolean processing = new AtomicBoolean(false);

    private boolean motionDetected;


    public Camera() {
//        this.preview = preview;
//        previewHolder = preview.getHolder();
//        previewHolder.addCallback(surfaceCallback);
//        previewHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        detector = new RgbMotionDetection();

//        detector.setMotionStateChangeListener(new MotionStateChangeListener() {
//            @Override
//            public void onMotionStateChange(final boolean motion) {
//
//                motionDetected = motion; // update internal state
//                System.out.println(motionDetected);
//
////                runOnUiThread(new Runnable() {
////                    @Override
////                    public void run() {
////                        System.out.println("State: " + motion);
////                        if (motion) {
////                            tv.setText("Motion");
////                        } else {
////                            tv.setText("No Motion");
////                        }
////
////                    }
////                });
//            }
//        });

    }

    // This method MUST be called before start sensing motion
    public void setSurfacePreview(SurfaceView preview) {
        this.preview = preview;
        previewHolder = preview.getHolder();
        previewHolder.addCallback(surfaceCallback);
        previewHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public boolean isMotionDetected() {
        return motionDetected;
    }
    
    @Override
    public void startSensing() {
        camera = android.hardware.Camera.open();
//        previewHolder.addCallback(surfaceCallback);
//        camera.setPreviewCallback(previewCallback);
    }

    @Override
    public void stopSensing() {
        camera.setPreviewCallback(null);
        if (inPreview) camera.stopPreview();
        inPreview = false;
        camera.release();
        camera = null;
    }

    @Override
    public void setOnDataSensedListener(Object listener) {
        detector.setMotionStateChangeListener((MotionStateChangeListener) listener);
    }

    private android.hardware.Camera.PreviewCallback previewCallback = new android.hardware.Camera.PreviewCallback() {

        /**
         * {@inheritDoc}
         */
        @Override
        public void onPreviewFrame(byte[] data, android.hardware.Camera cam) {
            if (data == null) return;
            android.hardware.Camera.Size size = cam.getParameters().getPreviewSize();
            if (size == null) return;

//            if (!GlobalData.isPhoneInMotion()) {
            DetectionThread thread = new DetectionThread(data, size.width, size.height);
            thread.start();
//            }
        }
    };

    private SurfaceHolder.Callback surfaceCallback = new SurfaceHolder.Callback() {

        /**
         * {@inheritDoc}
         */
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            try {
                camera.setPreviewDisplay(previewHolder);
                camera.setPreviewCallback(previewCallback);
            } catch (Throwable t) {
                Log.e("PreviewDemo-surfCall", "Exception in setPreviewDisplay()", t);
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            android.hardware.Camera.Parameters parameters = camera.getParameters();
            android.hardware.Camera.Size size = getBestPreviewSize(width, height, parameters);
            if (size != null) {
                parameters.setPreviewSize(size.width, size.height);
//                Log.d(TAG, "Using width=" + size.width + " height=" + size.height);
            }
            camera.setParameters(parameters);
            camera.startPreview();
            inPreview = true;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            // Ignore
        }
    };

    private static android.hardware.Camera.Size getBestPreviewSize(int width, int height, android.hardware.Camera.Parameters parameters) {
        android.hardware.Camera.Size result = null;

        for (android.hardware.Camera.Size size : parameters.getSupportedPreviewSizes()) {
            if (size.width <= width && size.height <= height) {
                if (result == null) {
                    result = size;
                } else {
                    int resultArea = result.width * result.height;
                    int newArea = size.width * size.height;

                    if (newArea > resultArea) result = size;
                }
            }
        }

        return result;
    }

    private final class DetectionThread extends Thread {

        private byte[] data;
        private int width;
        private int height;

        public DetectionThread(byte[] data, int width, int height) {
            this.data = data;
            this.width = width;
            this.height = height;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void run() {
            if (!processing.compareAndSet(false, true)) return;

            // Log.d(TAG, "BEGIN PROCESSING...");
            try {
                // Previous frame
//                int[] pre = null;
//                if (Preferences.SAVE_PREVIOUS) pre = detector.getPrevious();

                // Current frame (with changes)
                // long bConversion = System.currentTimeMillis();
                int[] img = null;
//                if (Preferences.USE_RGB) {
                img = ImageProcessing.decodeYUV420SPtoRGB(data, width, height);
//                } else {
//                    img = ImageProcessing.decodeYUV420SPtoLuma(data, width, height);
//                }

                // long aConversion = System.currentTimeMillis();
                // Log.d(TAG, "Converstion="+(aConversion-bConversion));

                // Current frame (without changes)
//                int[] org = null;
//                if (Preferences.SAVE_ORIGINAL && img != null) org = img.clone();

                if (img != null) {
//                    motionDetected = detector.detect(img, width, height);
                    motionDetected = detector.detect(img, width, height);
                }

//                if (img != null && detector.detect(img, width, height)) {
//
//                    // The delay is necessary to avoid taking a picture while in
//                    // the
//                    // middle of taking another. This problem can causes some
//                    // phones
//                    // to reboot.
//                    long now = System.currentTimeMillis();
//                    if (now > (mReferenceTime + Preferences.PICTURE_DELAY)) {
//                        mReferenceTime = now;
//
//                        Bitmap previous = null;
//                        if (Preferences.SAVE_PREVIOUS && pre != null) {
//                            if (Preferences.USE_RGB)
//                                previous = ImageProcessing.rgbToBitmap(pre, width, height);
//                            else previous = ImageProcessing.lumaToGreyscale(pre, width, height);
//                        }
//
//                        Bitmap original = null;
//                        if (Preferences.SAVE_ORIGINAL && org != null) {
//                            if (Preferences.USE_RGB)
//                                original = ImageProcessing.rgbToBitmap(org, width, height);
//                            else original = ImageProcessing.lumaToGreyscale(org, width, height);
//                        }
//
//                        Bitmap bitmap = null;
//                        if (Preferences.SAVE_CHANGES) {
//                            if (Preferences.USE_RGB)
//                                bitmap = ImageProcessing.rgbToBitmap(img, width, height);
//                            else bitmap = ImageProcessing.lumaToGreyscale(img, width, height);
//                        }
//
////                        Log.i(TAG, "Saving.. previous=" + previous + " original=" + original + " bitmap=" + bitmap);
//                        Looper.prepare();
////                        new SavePhotoTask().execute(previous, original, bitmap);
//                    } else {
//                        Log.i(TAG, "Not taking picture because not enough time has passed since the creation of the Surface");
//                    }
//                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                processing.set(false);
            }
            // Log.d(TAG, "END PROCESSING...");

            processing.set(false);
        }
    }

//    public void setMotionStateChangeListener(MotionStateChangeListener motionStateChangeListener) {
//        detector.setMotionStateChangeListener(motionStateChangeListener);
//    }

}
