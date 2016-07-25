package com.luisrdm.firebaseandsocialloginentregable.util;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

/*
 * Created by digitalhouse on 7/15/16.
 */
public class EasyImage {

    private static EasyImage instance = null;

    private static final String TAG = "EasyImage";
    private static final String KEY = "SDPermission";
    private static final String PROJECT = "EasyImage";

    private final int PICK_IMAGE_REQUEST = 1;
    private final int TAKE_IMAGE_REQUEST = 11;

    private static final int WRITE_PERMISSION = 55;

    private Activity contextActivity;
    private Fragment contextFragment;

    private Bitmap bitmap;
    private Bitmap bitmapResult;
    private Bitmap originalBitmap;

    private ImageView imageResult;

    private File file;

    private int imageWidth;
    private int imageHeight;

    private Integer sizeWidth;
    private Integer sizeHeight;

    private boolean isResultOk;

    private imageSize sizeSelect;

    private String imageName;

    private int requestCode;
    private Intent dataUri;

    /**
     * EN: List of available sizes to resize the result image.
     * <p/>
     * ES: Lista de tamaños disponibles para ajustar la imagen obtenida.
     * <li>{@link #IMAGE_FULL}</li>
     * <li>{@link #IMAGE_MEDIUM}</li>
     * <li>{@link #IMAGE_THUMBNAIL}</li>
     */
    public enum imageSize {
        /**
         * EN: Getting full size of image with a the limits
         * of device screen resolution.
         * <p/>
         * ES: Obtiene el tamaño completo de la imagen con
         * limites de la resolucion de pantalla del dispositivo.
         */
        IMAGE_FULL,
        /**
         * EN: Getting half of {@link #IMAGE_FULL} size of image with a the limits
         * of device screen resolution.
         * <p/>
         * ES: Obtiene la mitad del tamaño completo de {@link #IMAGE_FULL} con
         * limites de la resolucion de pantalla del dispositivo.
         */
        IMAGE_MEDIUM,
        /**
         * EN: Getting quarter of {@link #IMAGE_FULL} size of image with a the limits
         * of device screen resolution.
         * <p/>
         * ES: Obtiene un cuarto del tamaño completo de {@link #IMAGE_FULL} con
         * limites de la resolucion de pantalla del dispositivo.
         */
        IMAGE_THUMBNAIL
    }


    /**
     * EN: Singleton for {@link EasyImage EasyImage class}
     * <p/>
     * ES: Singleton de {@link EasyImage EasyImage class}
     *
     * @return EN: Single instance of {@link EasyImage EasyImage class}<br>
     * ES: Instancia unica de {@link EasyImage EasyImage class}
     */
    public static EasyImage getInstance() {
        if (instance == null) {
            instance = new EasyImage();
        }
        return instance;
    }

    /**
     * EN: Intent that request the Gallery or Galleries available to get a single image.
     * <p/>
     * ES: Intent el cual solicita una unica imagen a la Galeria o Galerias disponibles.
     *
     * @param activity EN: Current activity waiting for a result.
     *                 <br>
     *                 ES: Actividad vigente de la cual se espera un resultado.
     */
    public void openGallery(Activity activity) {
        isResultOk = false;
        this.contextActivity = activity;
        Intent intent = new Intent().setType("image/*").setAction(Intent.ACTION_GET_CONTENT);
        activity.startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
    }

    /**
     * EN: Intent that request the Gallery or Galleries available to get a single image.
     * <p/>
     * ES: Intent el cual solicita una unica imagen a la Galeria o Galerias disponibles.
     *
     * @param fragment EN: Current fragment waiting for a result.
     *                 <br>
     *                 ES: Fragment vigente del cual se espera un resultado.
     */
    public void openGallery(Fragment fragment) {
        isResultOk = false;
        this.contextFragment = fragment;
        this.contextActivity = fragment.getActivity();
        Intent intent = new Intent().setType("image/*").setAction(Intent.ACTION_GET_CONTENT);
        fragment.startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
    }

    /**
     * EN: Intent that request the Camera or Cameras available to get a single image.
     * It also checks if the devices has a front or rear camera available.
     * <p/>
     * ES: Intent el cual solicita una unica imagen a la Camara o Camaras disponibles.
     * Ademas, chequea si hay camara delantera o trasera disponibles.
     *
     * @param activity EN: Current activity waiting for a result.
     *                 <br>
     *                 ES: Actividad vigente de la cual se espera un resultado.
     */
    public void openCamera(Activity activity) {
        isResultOk = false;
        this.contextActivity = activity;
        this.imageName = TAG + System.currentTimeMillis() + ".jpg";
        if (hasUsesPermission(contextActivity)) {
            File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            String pictureImagePath = storageDir.getAbsolutePath() + "/" + imageName;
            file = new File(pictureImagePath);
            PackageManager pm = contextActivity.getPackageManager();

            if (pm.hasSystemFeature(PackageManager.FEATURE_CAMERA) || pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                activity.startActivityForResult(cameraIntent, TAKE_IMAGE_REQUEST);
            } else {
                Toast.makeText(activity, "No hay camaras disponible", Toast.LENGTH_SHORT).show();
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                setUsesPermission();
            } else {
                Toast.makeText(contextActivity, "No tienes los permisos necesarios en tu AndroidManifest.xml", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * EN: Intent that request the Camera or Cameras available to get a single image.
     * It also checks if the devices has a front or rear camera available.
     * <p/>
     * ES: Intent el cual solicita una unica imagen a la Camara o Camaras disponibles.
     * Ademas, chequea si hay camara delantera o trasera disponibles.
     *
     * @param fragment EN: Current fragment waiting for a result.
     *                 <br>
     *                 ES: Fragment vigente del cual se espera un resultado.
     */
    public void openCamera(Fragment fragment) {
        isResultOk = false;
        this.contextFragment = fragment;
        this.contextActivity = fragment.getActivity();
        this.imageName = TAG + System.currentTimeMillis() + ".jpg";
        if (hasUsesPermission(contextActivity)) {
            File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            String pictureImagePath = storageDir.getAbsolutePath() + "/" + imageName;
            file = new File(pictureImagePath);
            PackageManager pm = contextActivity.getPackageManager();

            if (pm.hasSystemFeature(PackageManager.FEATURE_CAMERA) || pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                fragment.startActivityForResult(cameraIntent, TAKE_IMAGE_REQUEST);
            } else {
                Toast.makeText(contextActivity, "No hay camaras disponible", Toast.LENGTH_SHORT).show();
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                setUsesPermission();
            } else {
                Toast.makeText(contextActivity, "No tienes los permisos necesarios en tu AndroidManifest.xml", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * EN: Dialog selector to execute the Gallery or Camera Intent.
     * <p/>
     * ES: Dialogo para seleccionar los Intent de Galeria o Camara.
     *
     * @param activity EN: Current activity waiting for a result.
     *                 <br>
     *                 ES: Actividad vigente de la cual se espera un resultado.
     */
    public void openSelector(Activity activity) {
        this.contextActivity = activity;
        AlertDialog.Builder alert = new AlertDialog.Builder(activity);
        alert.setTitle("OBTENER UNA IMAGEN:");
        alert.setItems(new CharSequence[]{"DESDE LA GALERIA", "DESDE LA CAMARA"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        openGallery(contextActivity);
                        break;
                    case 1:
                        openCamera(contextActivity);
                        break;
                    default:
                        break;
                }
            }
        }).show();
    }

    /**
     * EN: Dialog selector to execute the Gallery or Camera Intent.
     * <p/>
     * ES: Dialogo para seleccionar los Intent de Galeria o Camara.
     *
     * @param fragment EN: Current fragment waiting for a result.
     *                 <br>
     *                 ES: Fragment vigente del cual se espera un resultado.
     */
    public void openSelector(Fragment fragment) {
        this.contextActivity = fragment.getActivity();
        AlertDialog.Builder alert = new AlertDialog.Builder(contextActivity);
        alert.setTitle("OBTENER UNA IMAGEN:");
        alert.setItems(new CharSequence[]{"DESDE LA GALERIA", "DESDE LA CAMARA"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        openGallery(contextFragment);
                        break;
                    case 1:
                        openCamera(contextFragment);
                        break;
                    default:
                        break;
                }
            }
        }).show();
    }

    /**
     * EN: Gets the Display Metrics and Sets the Width and Height of it.
     * <p/>
     * ES: Obtiene las Metricas de la Pantalla y Setea Ancho y Alto.
     *
     * @param activity EN: Current Activity.<br>
     *                 ES: Activity en curso.
     */
    public EasyImage with(Activity activity) {
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        imageHeight = metrics.heightPixels;
        imageWidth = metrics.widthPixels;
        return this;
    }

    /**
     * EN: Sets the ImageView to apply the modify (or not) final image to it.
     * <p/>
     * ES: Setea el ImageView al cual aplicar la imagen final modificada (o no).
     *
     * @param view EN: The ImageView to apply the final Image.<br>
     *             ES: El ImageView al cual aplicar la imagen final.
     */
    public EasyImage into(ImageView view) {
        this.imageResult = view;
        return this;
    }

    /**
     * EN: Method to resize the final image in {@link imageSize three different sizes}.
     * If not set, it loads the default values.
     * <p/>
     * ES: Metodo que ajusta la imagen final en {@link imageSize tres tamaños diferentes}.
     * Si no se utiliza, se cargan los valores por default.
     *
     * @param size EN: Set the {@link imageSize imageSize}<br>
     *             ES: Setea el {@link imageSize imageSize}
     */
    public EasyImage resize(imageSize size) {
        this.sizeSelect = size;
        if (isResultOk) {
            switch (size) {
                case IMAGE_FULL:
                    bitmap = scaleBitmap(bitmap, imageWidth, imageHeight);
                    break;
                case IMAGE_MEDIUM:
                    bitmap = scaleBitmap(bitmap, imageWidth / 2, imageHeight / 2);
                    break;
                case IMAGE_THUMBNAIL:
                    bitmap = scaleBitmap(bitmap, imageWidth / 4, imageHeight / 4);
                    break;
                default:
                    break;
            }
            customSize(sizeWidth, sizeHeight);
        }
        return this;
    }

    /**
     * EN: Method to resize the final image with the desire Width and Height.
     * If not set, it loads the default values.
     * <p/>
     * ES: Metodo que permite ajustar la imagen final al tamaño deseado.
     * Si no se utiliza, se cargan los valores por default.
     *
     * @param width  EN: Final width for the new image.<br>
     *               ES: Ancho final para la nueva imagen.
     *               <p/>
     * @param height EN: Final height for the new image.<br>
     *               ES: Alto final para la nueva imagen.
     */
    public EasyImage customSize(Integer width, Integer height) {
        this.sizeWidth = width;
        this.sizeHeight = height;
        if (isResultOk) {
            if (sizeWidth != null && sizeHeight != null) {
                bitmap = Bitmap.createScaledBitmap(bitmap, sizeWidth, sizeHeight, false);
            }
            load();
        }
        return this;
    }

    /**
     * EN: Loads the final modified (or not) image to the desired ImageView.
     * <p/>
     * ES: Carga la imagen modificada (o no) en el ImageView deseado.
     */
    private void load() {
        if (isResultOk && imageResult != null) {
            imageResult.setImageBitmap(bitmap);
        } else {
            Toast.makeText(contextActivity, "Asegurate de declarar un ImageView", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * EN: Allows you to get the compress size image with
     * all the modified properties.
     * <p/>
     * ES: Permite obtener la imagen comprimida con todas sus
     * propiedades modificadas.
     *
     * @return EN: The compress size image.<br>
     * ES: La imagen comprimida.
     */
    public Bitmap getBitmap() {
        if (isResultOk) {
            return bitmap;
        }
        return null;
    }

    /**
     * EN: Allows you to get the original size image with
     * all the original properties.
     * <p/>
     * ES: Permite obtener la imagen original con todas sus
     * propiedades.
     *
     * @return EN: The original size image.<br>
     * ES: La imagen de tamaño original.
     */
    public Bitmap getOriginalBitmap() {
        if (isResultOk) {
            return originalBitmap;
        }
        return null;
    }

    public String getFileName() {
        if (isResultOk) {
            if (requestCode == PICK_IMAGE_REQUEST) {
                return new File(dataUri.getData().toString()).getName();
            } else {
                return getPhotoUri(imageName).toString();
            }
        } else {
            return "No se pudo conseguir nombre de la imagen";
        }
    }

    /**
     * EN: This method matches the onActivityResult from Activity or Gallery
     * to get all the necessary information of the returning image.
     * <p/>
     * ES: Este metodo tiene los mismos parametros del onActivityResult original
     * para conseguir la informacion necesaria de la imagen obtenida.
     *
     * @param requestCode EN: Code from Gallery source or Camera souce.<br>
     *                    ES: Codigo desde la Galerya o desde la Camara.
     *                    <p/>
     * @param resultCode  EN: Check if the result was OK or not.<br>
     *                    ES: Chequea si el resultado fue OK o no.
     *                    <p/>
     * @param data        EN: The raw data of the imagen location.<br>
     *                    ES: La informacion de la ubicacion de la imagen.
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        this.requestCode = requestCode;
        this.dataUri = data;
        if ((requestCode == PICK_IMAGE_REQUEST || requestCode == TAKE_IMAGE_REQUEST) && resultCode == -1) {
            isResultOk = true;
            try {
                if (bitmap != null && bitmapResult != null && originalBitmap != null) {
                    bitmap.recycle();
                    bitmap = null;
                    bitmapResult.recycle();
                    bitmapResult = null;
                    originalBitmap.recycle();
                    originalBitmap = null;
                    imageResult.setImageDrawable(null);
                }

                if (requestCode == TAKE_IMAGE_REQUEST) {
                    Uri takenPhotoUri = getPhotoUri(imageName);
                    bitmapResult = BitmapFactory.decodeFile(takenPhotoUri.getPath());
                } else {
                    bitmapResult = MediaStore.Images.Media.getBitmap(contextActivity.getContentResolver(), data.getData());
                }

                bitmap = scaleBitmap(bitmapResult, imageWidth, imageHeight);
                originalBitmap = bitmapResult;
                resize(sizeSelect == null ? imageSize.IMAGE_MEDIUM : sizeSelect);

                if (requestCode == TAKE_IMAGE_REQUEST) {
                    if (file.exists()) {
                        if (file.delete()) {
                            Log.d(TAG, "Image successfully deleted");
                        } else {
                            Log.d(TAG, "Error while trying to delete the image");
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * EN: This method returns the final Uri path of the images capture.
     * <p>
     * ES: Este metodo regresa la ruta Uri final de la imagen capturada.
     * @param fileName EN: request the file get to get access
     *                 <br>
     *                 ES: solicita el archivo al cual tener acceso.
     * @return EN: Final Uri path of the image.
     *         <br>
     *         ES: Ruta Uri final de la imagen.
     */
    public Uri getPhotoUri(String fileName) {
        return Uri.fromFile(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath() + File.separator + fileName));
    }

    /**
     * EN: Allows you to scale an Image keeping ratio while doing it.
     * <p/>
     * ES: Permite escalar una imagen y mantener la proporcion de la misma.
     *
     * @param image     EN: Bitmap image to work the scale.<br>
     *                  ES: Imagen bitmap sobre la cual realizar el escalado.
     *                  <p/>
     * @param maxWidth  EN: Max Integer value for the Width of the image.<br>
     *                  ES: Maximo valor Integer para el Ancho de la imagen.
     *                  <p/>
     * @param maxHeight EN: Max integer value for the Height of the image.<br>
     *                  ES: Maximo valor Integer para el Alto de la imagen.
     * @return EN: The scaled image from the original file.<br>
     * ES: La imagen escalada desde la original.
     */
    private Bitmap scaleBitmap(Bitmap image, int maxWidth, int maxHeight) {
        if (image.getWidth() < maxWidth && image.getHeight() < maxHeight) {
            return image;
        } else {
            if (maxHeight > 0 && maxWidth > 0) {
                float ratioBitmap = (float) image.getWidth() / (float) image.getHeight();
                float ratioMax = (float) maxWidth / (float) maxHeight;

                int finalWidth = maxWidth;
                int finalHeight = maxHeight;

                if (ratioMax > 1) {
                    finalWidth = (int) ((float) maxHeight * ratioBitmap);
                } else {
                    finalHeight = (int) ((float) maxWidth / ratioBitmap);
                }

                image = Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true);
                return image;
            } else {
                return image;
            }
        }
    }

    /**
     * EN: Method that gets all the onRequestPermissionResult parameters to valid
     * the uses permission by the user.
     * If the user grant the permission it'll saves true, if not, saves false.
     * <p/>
     * ES: Metodo que consigue todos los parametros de onRequestPermissionResult
     * para validar el uses permission del usuario.
     * Si el usuario valida el permiso guarda true, si no, guarda false.
     */
    public void onRequestPermissionResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case WRITE_PERMISSION: {
                SharedPreferences settings = contextActivity.getSharedPreferences(PROJECT, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean(KEY, grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED);
                editor.apply();
                if (contextFragment != null) {
                    openCamera(contextFragment);
                } else {
                    openCamera(contextActivity);
                }
                break;
            }
        }
    }

    /**
     * EN: This method ask for Uses Permission if the API is >= 23.
     * <p/>
     * ES: Este metodo pregunta por Uses Permission si la API es >=23.
     */
    private void setUsesPermission() {
        if (ContextCompat.checkSelfPermission(contextActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(contextActivity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(contextActivity, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_PERMISSION);
        }
    }

    /**
     * EN: Method that checks if uses permission CAMERA && WRITE_EXTERNAL_STORAGE is set
     * on the AndroidManifest.xml
     * <p/>
     * ES: Metodo que chequea si el uses permission CAMERA && WRITE_EXTERNAL_STORAGE esta
     * seteado en el AndroidManifest.xml
     *
     * @param activity EN: Current activity waiting for a result.
     *                 <br>
     *                 ES: Actividad vigente de la cual se espera un resultado.
     * @return EN: "true" if has uses permission CAMERA && WRITE_EXTERNAL_STORAGE is set on the AndroidManifest.xml
     * <br>
     * ES: "true" si tiene uses permission CAMERA && WRITE_EXTERNAL_STORAGE en el AndroidManifest.xml
     */
    private boolean hasUsesPermission(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            SharedPreferences settings = contextActivity.getSharedPreferences(PROJECT, Context.MODE_PRIVATE);
            return settings.getBoolean(KEY, false);
        } else {
            int writeExternal = activity.checkCallingOrSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            return writeExternal == PackageManager.PERMISSION_GRANTED;
        }
    }
}