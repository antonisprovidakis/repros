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

//    public static int PICTURE_DELAY = 200; // ms

    private SurfaceView preview = null;
    private SurfaceHolder previewHolder = null;
    private android.hardware.Camera camera = null;
    private boolean inPreview = false;
    private RgbMotionDetection detector = null;
    private volatile AtomicBoolean processing = new AtomicBoolean(false);

    private boolean motionDetected;

    public Camera() {
        detector = new RgbMotionDetection();
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

            DetectionThread thread = new DetectionThread(data, size.width, size.height);
            thread.start();
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

            try {
                int[] img = null;
                img = ImageProcessing.decodeYUV420SPtoRGB(data, width, height);
                if (img != null) {
                    motionDetected = detector.detect(img, width, height);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                processing.set(false);
            }
            processing.set(false);
        }
    }
}
