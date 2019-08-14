package com.example.live;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.IOException;
import java.util.Date;

import static android.app.Activity.RESULT_OK;
import static androidx.constraintlayout.widget.Constraints.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewPostFragment extends Fragment {
    private static final int READ_REQUEST_CODE = 42;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    final static int RESAULT_SUCCESS = 0;

    ImageView selectedPictureImgView;
    EditText postTitleEditText;
    EditText postDescriptionEditText;

    private FirebaseStorage mStorage;
    private FirebaseFirestore mDb;
    private boolean hasImageBeenSet = false;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    public NewPostFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            setPostImage(imageBitmap);
        }

        // The ACTION_OPEN_DOCUMENT intent was sent with the request code
        // READ_REQUEST_CODE. If the request code seen here doesn't match, it's the
        // response to some other intent, and the code below shouldn't run at all.

        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // The document selected by the user won't be returned in the intent.
            // Instead, a URI to that document will be contained in the return intent
            // provided to this method as a parameter.
            // Pull that URI using resultData.getData().
            Uri uri = null;
            if (data != null) {
                uri = data.getData();
                Log.i(TAG, "Uri: " + uri.toString());
                try {
                    Bitmap imageBitmap = getBitmapFromUri(uri);
                    setPostImage(imageBitmap);
                }
                catch (Exception ex) {
                    Toast.makeText(getContext(), "Failed to load picture. Make sure the selected photo is a real photo or try a different one.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private void setPostImage(Bitmap imageBitmap) {
        this.hasImageBeenSet = true;
        selectedPictureImgView.setImageBitmap(imageBitmap);
        selectedPictureImgView.setVisibility(View.VISIBLE);
    }

    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getActivity().getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_new_post, container, false);

        Button uploadPhotoButton = fragmentView.findViewById(R.id.newPostUploadPhotoButton);
        this.selectedPictureImgView = fragmentView.findViewById(R.id.selectedPictureImgView);
        this.postTitleEditText = fragmentView.findViewById(R.id.newPostTitleTxt);
        this.postDescriptionEditText = fragmentView.findViewById(R.id.newPostDescriptionTxt);

        uploadPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getContext().getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        });

        fragmentView.findViewById(R.id.newPostTakePictureButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performFileSearch();
            }
        });

        fragmentView.findViewById(R.id.newPostSubmitButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isPostInvalid = false;

                CharSequence postTitle = postTitleEditText.getText();
                CharSequence postDescription = postDescriptionEditText.getText();

                if (TextUtils.isEmpty(postTitle)) {
                    isPostInvalid = true;
                    postTitleEditText.setError("Title must not be empty!");
                }

                if (TextUtils.isEmpty(postDescription) && !hasImageBeenSet) {
                    isPostInvalid = true;
                    postDescriptionEditText.setError("A post must have a description, or an image that describes it.");
                }

                if (!isPostInvalid) {
                    mDb = FirebaseFirestore.getInstance();

                    CollectionReference postsCollectionRef = mDb.collection("posts");

                    DocumentReference documentReference = postsCollectionRef.document();
                    String newPostId = documentReference.getId();

                    Post post = new Post(newPostId, postTitle.toString(), postDescription.toString(), null, user.getEmail(), new Date());
                    documentReference.set(post).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "New post added with ID: " + newPostId);

                            if (hasImageBeenSet) {
                                uploadPostImgToFirestore(newPostId, documentReference, fragmentView);
                            } else {
                                onNewPostCreated(fragmentView);
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error adding document", e);
                            onPostCreationFailed();
                        }
                    });
                }
            }
        });

        return fragmentView;
    }

    private void uploadPostImgToFirestore(String newPostId, DocumentReference docRef, View fragmentView) {
        mStorage = FirebaseStorage.getInstance();

        // Create a storage reference from our app
        StorageReference storageRef = mStorage.getReference();

        // Create a reference to the new picture we want to upload
        StorageReference picRef = storageRef.child("post_images/" + newPostId);

        // Get the data from an ImageView as bytes
        selectedPictureImgView.setDrawingCacheEnabled(true);
        selectedPictureImgView.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) selectedPictureImgView.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = picRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                onPostPhotoUploadFailed();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                picRef.getDownloadUrl().addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        onGetDownloadUrlFailed();
                    }
                }).addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        ImageCache.verifyStoragePermissions(getActivity());
                        ImageCache.saveImageToFile(bitmap, ImageCache.UriToStringConverter(uri));
                        linkPostPhotoToPost(newPostId, docRef, uri, fragmentView);
                    }
                });
            }
        });
    }

    private void onGetDownloadUrlFailed() {
        Toast.makeText(getContext(), "Failed to get the post image. Make sure you have a working active internet connection.", Toast.LENGTH_LONG).show();
    }

    private void linkPostPhotoToPost(String newPostId, DocumentReference docRef, Uri photoUri, View fragmentView) {
        docRef.update("image", photoUri.toString()).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                onLinkPostToImageFailed();
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                onNewPostCreated(fragmentView);
            }
        });
    }

    private void onLinkPostToImageFailed() {
        Toast.makeText(getContext(), "Failed to save the post. Make sure you have a working active internet connection.", Toast.LENGTH_LONG).show();
    }

    private void onPostPhotoUploadFailed() {
        Toast.makeText(getContext(), "There was an issue with uploading the photo.", Toast.LENGTH_LONG).show();
    }

    private void onPostCreationFailed() {
        Toast.makeText(getContext(), "Failed to create post. Make sure you have a working internet connection.", Toast.LENGTH_LONG).show();
    }

    private void onNewPostCreated(View fragmentView) {
        Toast.makeText(getContext(), "Ze Avad Omg!..!.!!. *-* ^-^", Toast.LENGTH_LONG).show();
        Navigation.findNavController(fragmentView).navigate(R.id.nav_Post_Fragment);
    }

    /**
     * Fires an intent to spin up the "file chooser" UI and select an image.
     */
    private void performFileSearch() {

        // ACTION_OPEN_DOCUMENT is the intent to choose a file via the system's file
        // browser.
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);

        // Filter to only show results that can be "opened", such as a
        // file (as opposed to a list of contacts or timezones)
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        // Filter to show only images, using the image MIME data type.
        // If one wanted to search for ogg vorbis files, the type would be "audio/ogg".
        // To search for all documents available via installed storage providers,
        // it would be "*/*".
        intent.setType("image/*");

        startActivityForResult(intent, READ_REQUEST_CODE);
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        Log.d("tag", "searchview BEGONE from new post");
        super.onCreate(savedInstanceState); setHasOptionsMenu(true);
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        Log.d("tag", "on preperation of new posts ydig");
        menu.findItem(R.id.post_search).setVisible(false);
        super.onPrepareOptionsMenu(menu);
    }
}
