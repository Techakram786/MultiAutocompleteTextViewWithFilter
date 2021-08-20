  package com.techakram.Online_Shop;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class DBQueries {
    public static FirebaseFirestore db = FirebaseFirestore.getInstance( );
    public static List<CatagoryModel> catagoryModelList = new ArrayList<>( );
    //public static List<HomePageModel> homePageModelList=new ArrayList<>();
    public static List<List<HomePageModel>> lists=new ArrayList<>();
    public static List<String> loadCategoriesName=new ArrayList<>();

    public static void loadCategory(final CatagoryAdapter catagoryAdapter, final Context context) {
        db.collection("CATEGORIES").orderBy("index").get( )
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>( ) {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful( )) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult( )) {
                                catagoryModelList.add(new CatagoryModel(documentSnapshot.get("icon").toString( ),
                                        documentSnapshot.get("CategoryName").toString( )));
                            }
                            catagoryAdapter.notifyDataSetChanged( );
                        } else {
                            String error = task.getException( ).getMessage( );
                            Toast.makeText(context, error, Toast.LENGTH_SHORT).show( );
                        }

                    }
                });
    }

    public static void loadFragmentData(HomePageAdapter adapter, Context context,final int index,String categoryName) {
        db.collection("CATEGORIES")
                .document(categoryName.toUpperCase()).collection("TOP_DEALS")
                .orderBy("index").get( ).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>( ) {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful( )) {
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult( ))
                        if ((long) documentSnapshot.get("view_Type") == 0) {
                            List<SliderModel> sliderModelList = new ArrayList<>( );
                            long no_of_banner = (long) documentSnapshot.get("no_of_banners");
                            for (long x = 1; x < no_of_banner + 1; x++) {
                                sliderModelList.add(new SliderModel
                                        (documentSnapshot.get("banner_" + x).toString( ),
                                                documentSnapshot.get("banner_" + x + "_background").toString( )));
                            }
                           lists.get(index).add(new HomePageModel(0, sliderModelList));
                        } else if ((long) documentSnapshot.get("view_Type") == 1) {
                            lists.get(index).add(new HomePageModel
                                    (1, documentSnapshot.get("strip_ad_banner").toString( )
                                            , documentSnapshot.get("background").toString( )));
                        } else if ((long) documentSnapshot.get("view_Type") == 2) {
                            List<WishListModel> viewAllProductList=new ArrayList<>();
                            List<Horizontal_product_scroll_Model> horizontal_product_scroll_modelList = new ArrayList<>( );
                            long no_of_product = (long) documentSnapshot.get("no_of_product");
                            for (long x = 1; x < no_of_product + 1; x++) {
                                horizontal_product_scroll_modelList.add(new Horizontal_product_scroll_Model(
                                        documentSnapshot.get("product_id_" + x).toString( ),
                                        documentSnapshot.get("product_image_" + x).toString( ),
                                        documentSnapshot.get("product_title_" + x).toString( ),
                                        documentSnapshot.get("product_subtitle_" + x).toString( ),
                                        documentSnapshot.get("product_price_" + x).toString( )));
                                /////////////////////////////////for viewall button..
                                viewAllProductList.add(new WishListModel(documentSnapshot.get("product_image_"+x).toString(),

                                        (long)documentSnapshot.get("free_coupens_" + x),
                                        (long)documentSnapshot.get("total_rating_" + x),
                                        documentSnapshot.get("product_full_title_" + x).toString( ),
                                        documentSnapshot.get("average_rating_" + x).toString( ),
                                        documentSnapshot.get("product_price_" + x).toString( ),
                                        documentSnapshot.get("cutted_price_" + x).toString( ),
                                        (boolean)documentSnapshot.get("COD_" + x)));
                                //Log.d("ask", String.valueOf(viewallProductList.get(1)));
                            }
                            lists.get(index).add(new HomePageModel(2,
                                    documentSnapshot.get("layout_title").toString( ),
                                    documentSnapshot.get("layout_background").toString( ),
                                    horizontal_product_scroll_modelList,viewAllProductList));
                        } else if ((long) documentSnapshot.get("view_Type") == 3) {
                         List<Horizontal_product_scroll_Model> gridLayoutModelList=new
                                 ArrayList<>();
                         long no_of_product= (long) documentSnapshot.get("no_of_product");
                         for(long x=1; x< no_of_product+1; x++)
                         {
                             gridLayoutModelList.add(new Horizontal_product_scroll_Model(
                                     documentSnapshot.get("product_id_"+x).toString(),
                                     documentSnapshot.get("product_image_"+x).toString(),
                                     documentSnapshot.get("product_title_"+x).toString(),
                                     documentSnapshot.get("product_subtitle_"+x).toString(),
                                     documentSnapshot.get("product_price_"+x).toString()));
                         }
                            lists.get(index).add(new HomePageModel(3,
                                 documentSnapshot.get("layout_title").toString(),
                                 documentSnapshot.get("layout_background").toString(),
                                 gridLayoutModelList));
                        }


                    adapter.notifyDataSetChanged( );
                } else {
                    String error = task.getException( ).getMessage( );
                    Toast.makeText(context,error, Toast.LENGTH_SHORT).show( );
                }

            }
        });

    }
}