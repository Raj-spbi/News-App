package com.aaratechnologies.lawtrend.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.aaratechnologies.lawtrend.R;
import com.aaratechnologies.lawtrend.managers.VolleyMultipartRequest;
import com.aaratechnologies.lawtrend.managers.WebURLS;
import com.aaratechnologies.lawtrend.menuwiseactivities.OnlineInternshipActivity;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

public class ApplyForInternShipBottomSheet extends BottomSheetDialogFragment {
    final DatePickerDialog[] datePickerDialog = new DatePickerDialog[1];
    final int[] year = new int[1];
    final int[] month = new int[1];
    final int[] dayOfMonth = new int[1];
    final Calendar[] calendar = new Calendar[1];

    TextView to,imgname,dob;
    Button browse,applybtn;
    Bitmap bitmap;
    ProgressDialog progressDialog;
    String dateofbirth="",joiningdate="",AcademicProfessionalReferee="";
    TextInputEditText name,sex,mailingaddress,mobile,email,college,academic,academicprofessional,areaofinterest,how;

    View view;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.custom_apply_internship, container, false);


        progressDialog=new ProgressDialog(getContext());

        imgname=view.findViewById(R.id.imgname);

        name=view.findViewById(R.id.name);
        dob=view.findViewById(R.id.dob);
        sex=view.findViewById(R.id.sex);
        mailingaddress=view.findViewById(R.id.mailingaddress);
        mobile=view.findViewById(R.id.mobile);
        email=view.findViewById(R.id.email);
        browse=view.findViewById(R.id.browse);
        college=view.findViewById(R.id.college);
        academic=view.findViewById(R.id.academic);
        to=view.findViewById(R.id.to);
        applybtn=view.findViewById(R.id.applybtn);
        academicprofessional=view.findViewById(R.id.academicprofessional);
        areaofinterest=view.findViewById(R.id.areaofinterest);
        how=view.findViewById(R.id.how);

        to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar[0] =Calendar.getInstance();
                year[0] = calendar[0].get(Calendar.YEAR);
                month[0] = calendar[0].get(Calendar.MONTH);
                dayOfMonth[0] = calendar[0].get(Calendar.DAY_OF_MONTH);
                datePickerDialog[0] =new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        to.setText(year + "/"+ (month+1) +"/" +dayOfMonth);
                        joiningdate=to.getText().toString();
//                        Toast.makeText(getContext(), ""+joiningdate, Toast.LENGTH_SHORT).show();
                    }
                }, year[0], month[0], dayOfMonth[0]);
                datePickerDialog[0].show();

            }
        });
        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar[0] =Calendar.getInstance();
                year[0] = calendar[0].get(Calendar.YEAR);
                month[0] = calendar[0].get(Calendar.MONTH);
                dayOfMonth[0] = calendar[0].get(Calendar.DAY_OF_MONTH);
                datePickerDialog[0] =new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        dob.setText(year + "/"+ (month+1) +"/" +dayOfMonth);
                        dateofbirth=dob.getText().toString();
//                        Toast.makeText(getContext(), ""+dateofbirth, Toast.LENGTH_SHORT).show();
                    }
                }, year[0], month[0], dayOfMonth[0]);
                datePickerDialog[0].show();

            }
        });
        browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });
        applybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bitmap==null){
                    Toast.makeText(getContext(), "Haven't choose Image", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (name.getText().toString().trim().isEmpty()){
                    name.setError("Can't be Empty");
                    name.requestFocus();
                    return;
                }
                if (email.getText().toString().trim().isEmpty()){
                    email.setError("Can't be Empty");
                    email.requestFocus();
                    return;
                }
                if (mobile.getText().toString().trim().isEmpty()){
                    mobile.setError("Can't be Empty");
                    mobile.requestFocus();
                    return;
                }
                uploadBitmap(bitmap);
            }
        });
        return view;
    }
    private void openGallery() {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, 100);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {

            //getting the image Uri
            Uri imageUri = data.getData();
            String imagename=imageUri.getLastPathSegment();
            imgname.setText(imagename+".JPG");
            try {
                //getting bitmap object from uri
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);

                //displaying selected image to imageview
//                imgname.setImageBitmap(bitmap);

                //calling the method uploadBitmap to upload image
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }   public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 50, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private void uploadBitmap(final Bitmap bitmap) {
        //our custom volley request
        progressDialog.setMessage("Adding...");
        progressDialog.show();
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, WebURLS.Internship_FORM,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject=new JSONObject(new String(response.data));
                            Log.d("uploaddata", "onResponse: "+jsonObject);
//                            Toast.makeText(getContext(), ""+jsonObject, Toast.LENGTH_SHORT).show();
                            if (jsonObject.getString("status").equalsIgnoreCase("mail_sent")){
                                Toast.makeText(getContext(), ""+jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(getActivity(), OnlineInternshipActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            } else {
                                Toast.makeText(getContext(), "Something went Wrong ", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
//                        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                        final AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
                        builder.setMessage("Network Error \nPlease Try After Sometime");
                        builder.setTitle("Alert !");
                        builder.setCancelable(true);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                              //Nothing to be done
                                Intent intent=new Intent(getActivity(),OnlineInternshipActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        });
                        AlertDialog alertDialog=builder.create();
                        alertDialog.show();

                    }
                }) {

            /*
             * If you want to add more parameters with the image
             * you can do it here
             * here we have only one parameter with the image
             * which is tags
             * */
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> hm = new HashMap<>();
                hm.put("your-name", name.getText().toString().trim());
                hm.put("Dateofbirthddmmyyyy", dateofbirth);
                hm.put("sex",sex.getText().toString().trim());
                hm.put("your-email",mailingaddress.getText().toString().trim());
                hm.put("Mobile",mobile.getText().toString().trim());
                hm.put("Email",email.getText().toString().trim());
//                hm.put("browse",browse.getText().toString().trim());
                hm.put("NameofCollegeUniversity",college.getText().toString().trim());
                hm.put("Academicyear",academic.getText().toString().trim());
                hm.put("Expectedperiodofinternship",to.getText().toString());
                hm.put("AcademicProfessionalReferee",academicprofessional.getText().toString().trim());
                hm.put("Areaofinterest",areaofinterest.getText().toString().trim());
                hm.put("text-738",how.getText().toString().trim());
                return hm;
            }

            /*
             * Here we are passing image by renaming it with a unique name
             * */
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("AttachPassportsizePhoto", new DataPart(imagename + ".png", getFileDataFromDrawable(bitmap)));
                return params;
            }
        };

        //adding the request to volley
        Volley.newRequestQueue(getContext()).add(volleyMultipartRequest);
    }
}

