-optimizations   code/simplification/arithmetic,!code/simplification/cast,!field/*,! class/merging/*,!method/inlining/*
-optimizationpasses 5
-allowaccessmodification
-dontskipnonpubliclibraryclasses
-dontpreverify
-repackageclasses ''

-keepclassmembers class android.support.design.internal.BottomNavigationMenuView {
    boolean mShiftingMode;
}

 -keepclassmembers class * extends android.content.Context {
    public void *(android.view.View);
    public void *(android.view.MenuItem);
 }