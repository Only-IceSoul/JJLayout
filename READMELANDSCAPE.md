
# LANDSCAPE

## Using XML

**Active LandScape**:  
 app:support_landscape="true"

**ConstraintLayout**

cl = ConstraintLayout Portrait  
cll = ConstraintLayout LandScape

lp = just padding

**Other Views**

lp = Layout Params  
lpl = Layout Params LandScape

lp support : height , width , margin, padding

comming soon : gravity,etc..


    <com.jjlf.library_layout.views.JJConstraintLayout
                android:id="@+id/topView"
                android:layout_width="0dp"
                android:layout_height="0dp"

                app:support_landscape="true"

                app:clFillParentHorizontally="true"
                app:cllFillParentHorizontally="true"

                app:clHeightPercentScreenHeight="0.3"
                app:cllHeightPercentScreenWidth="0.3"

                app:clTopToTopParent="true"
                app:cllTopToTopParent="true"

                app:clMarginHorizontalPerScWidth="0.2"
                app:cllMarginHorizontalPerScHeight="0.45"

                app:clMarginTopPercentScreenHeight="0.1"
                app:cllMarginTopPercentScreenHeight="0.1"

                android:padding="20dp"
                app:lplPadding="5dp"


                android:background="@color/colorPrimary"
                tools:ignore="MissingConstraints" >
        
                <com.jjlf.library_layout.views.JJView
                        android:layout_width="0dp"
                        android:layout_height="0dp"
                        app:clFillParent="true"
                        android:background="@color/colorAccent"
                />

         </com.jjlf.library_layout.views.JJConstraintLayout>

        <com.jjlf.library_layout.views.JJView
                android:layout_width="0dp"
                android:layout_height="50dp"

                app:support_landscape="true"
                app:layout_height_landscape="50dp"

                app:clBottomToTopOf="@id/topView"
                app:clWidthResponsivePercentScreenWidth="@array/widthFloat"
                app:clCenterHorizontallyOf="@id/topView"

                app:cllBottomToTopOf="@id/topView"
                app:cllWidthResponsivePercentScreenWidth="@array/widthFloat"
                app:cllCenterHorizontallyOf="@id/topView"

                android:background="@color/colorAccent"
         tools:ignore="MissingConstraints"/>


 **Preview XML:**
  
  Portrait

 ![Preview](assets/landscape1.jpg)   

 LandScape

![Preview2](assets/landscape2.jpg)   



## **CONFIG CHANGES:**

Manifiest:

    <activity android:name=".landscape.LandScapeActivity"
             android:configChanges="screenLayout|orientation|screenSize"
                >


XML: 

        <com.jjlf.library_layout.views.JJConstraintLayout
             
                app:support_landscape="true"
                app:support_configuration_changed="true"

                //constraints or layout params

        tools:ignore="MissingConstraints" />

