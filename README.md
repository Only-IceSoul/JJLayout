# JJLayout

Create Android Apps faster!!!

## Usage

1.-Add it in your root build.gradle at the end of repositories:
```
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
2.- Add the dependency
```
dependencies {
	        implementation 'com.github.Only-IceSoul:JJLayout:1.6'
	}
```

## **Using XML** 

### **ConstrainLayout:**

[See all Attributes](https://github.com/Only-IceSoul/JJLayout/blob/master/library-layout/src/main/res/values/attrs.xml)

**cl** = Contraint Layout 

**lp** = just padding

 **Responsive:** ResPercentScreenHeight/Witdh (array-float[4]),   
 Responsive (array-dimension[4])  [Responsive](#Responsive)




    <JJConstraintLayout
        android:id="@+id/childView"
        android:layout_width="100dp"
        android:layout_height="0dp"
        android:background="@color/colorPrimary"
        app:clFillParentVertically="true"
        app:clCenterInParentHorizontally="true"
        app:clMarginTop="20dp"
        app:clMarginBottomPercentScreenHeight="0.4"
        />

     <JJImageView
        android:id="@+id/childView2"
        android:layout_width="100dp"
        android:layout_height="100dp"
        android:background="@color/colorAccent"
        app:clCenterInParent="true"
        />



**Preview XML:**

Force Preview: click force refresh layout or Re-build 

![Refresh](assets/refresh_preview.jpg)



![Preview](assets/cljjkit.jpg)


### **Other Views:**

[See all Attributes](https://github.com/Only-IceSoul/JJLayout/blob/master/library-layout/src/main/res/values/attrs.xml)

**lp** = Layout params

**Atributes:** Height, Width, Margin and Padding.

 **Responsive:** ResPercentScreenHeight/Witdh (array-float[4]),  
  Responsive (array-dimension[4]) [Responsive](#Responsive)


        <JJButton
            android:id="@+id/childView2"
            android:layout_width="100dp"
            android:layout_height="0dp"
            android:background="@color/colorAccent"
            app:lpHeightPercentScreenHeight="0.5"
            app:lpMarginTopPerScHeight="0.02"
        />


## **Using Programmatically**

#### Parent ConstraintLayout

```
       val childview = JJConstraintLayout(this)
        val childview2 = JJConstraintLayout(this)
        val parentView = JJConstraintLayout(this)

        parentView.addViews(childview,
                            childview2
                            )

        childview
            .clHeight(JJScreen.percentHeight(0.5f))
            .clFillParentHorizontally()
            .clTopToTopParent(20)
            .clMargins(JJMargin.left(50))
            .clApply()

        childview2
            .clHeight(JJScreen.dp(200))
            .clWidth(JJScreen.dp(200))
            .clCenterInParent()
            .clApply()

```

#### Other Views

**abl** = AppBarLayout

**col** = CoordinatorLayout

**sv** = ScrollView

**ll** = LinearLayout


```
        val childview = JJConstraintLayout(this)

        val parentView = JJLinearLayout(this)
                        .addViews(childview)
                        .ssOrientation(LinearLayout.VERTICAL)
                        .ssPadding(JJPadding.top(100))

        childview.llWidth(LinearLayout.LayoutParams.MATCH_PARENT)
            .llHeight(200)


```

### **Motion Layout**

**Just code**

Set = ss  
Get = gg

``` 
        val motionLayout = JJMotionLayout(this)
                         .ssLoadLayoutDescriptio(R.xml.scene_movingTest)


        val csStart = motionLayout.ggConstraintSet(R.id.start)
        val csEnd =  motionLayout.ggConstraintSet(R.id.end)

        val sizeBtn = JJScreen.percentHeight(0.06f)
        val paddingBtn = (sizeBtn * 0.35f).toInt()

        val mButtonImage = JJButtonImage(this)
        mButtonImage.id = R.id.childView

        motionLayout.addViews(mButtonImage,mTextView,separator)
        
        mButtonImage
            .ssImageDrawable(ContextCompat.getDrawable(context, R.drawable.svg_icon_test)
            )
            .ssScaleType(ImageView.ScaleType.FIT_CENTER)
            .ssBackground(JJBgColorDrawable(Color.WHITE,JJBgColorDrawable.CIRCLE)
            .ssOutlineProvider(
                JJOutlineProvider(JJOutlineProvider.CIRCLE)
                                .ssOffset(0,JJScreen.point(10))
                )
            .ssPadding(JJPadding(0,paddingBtn,0,paddingBtn))

            .mlSetConstraint(csStart)
            .mlHeight(sizeBtn).mlWidth(sizeBtn)
            .mlTopToTopParent(JJScreen.point(40))
            .mlStartToStartParent(JJScreen.point(80))
            .mlElevation(8f)
            .mlApplyConstraint()

            .mlSetConstraint(csEnd)
            .mlHeight(sizeBtn).mlWidth(sizeBtn)
            .mlTopToTopParent(JJScreen.point(40))
            .mlStartToStartParent(JJScreen.point(80))
            .mlElevation(8f)

            .mlDisposeConstraint()


```

**Motion Scene**

```
<MotionScene xmlns:motion="http://schemas.android.com/apk/res-auto"
        xmlns:android="http://schemas.android.com/apk/res/android">

    <Transition
            motion:constraintSetStart="@+id/start"
            motion:constraintSetEnd="@+id/end"
            motion:duration="1000"
            motion:motionInterpolator="linear">
            <OnSwipe
                motion:touchAnchorId="@+id/childView"
                motion:touchAnchorSide="bottom"
                motion:dragDirection="dragUp" />


        <ConstraintSet android:id="@+id/start">

        </ConstraintSet>

        <ConstraintSet android:id="@+id/end">

        </ConstraintSet>

    </Transition>

</MotionScene>

```

## Responsive

Based in Height

2600px to Infinite **xHigher**  
2001px to 2599px **Higher**  
1300px to 2000px **Medium**  
1px to 1299px **Small**  

[See Class JJScreen](https://github.com/Only-IceSoul/JJLayout/blob/master/library-layout/src/main/java/com/jjlf/library_layout/JJScreen.kt)

### XML

```
<resources>
     <array name="arrayDimen" >
         <item>300dp</item> xHigher  0
         <item>100dp</item> Higher  1
         <item>50dp</item> Medium  2
         <item>30dp</item> Small  3
         <item>60dp</item> ignored 4
     </array>

    <array name="arrayfloat" >
        <item>0.1</item> xHigher
        <item>0.2</item> Higher
        <item>0.3</item> Medium
        <item>0.4</item> Small
         <item>0.6</item> ignored
    </array>
</resources>

```

# Clip

Using canvas clip - Not anti-aliasing

# LANDSCAPE

[See Full Guide](READMELANDSCAPE.md)

# License

## Apache License 2.0

[See License](https://github.com/Only-IceSoul/JJLayout/blob/master/LICENSE)
