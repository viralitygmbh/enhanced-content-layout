# enhanced-content-layout
A wrapper layout which allows easy integration of swipe to refresh and showing progress and messages.

## Including the layout

### In Layout XML
Wrap the content you want to enhance in the EnhancedContentLayout. 
Kepp in mind that the wrapped views will be invisible when showing a message etc. (e.g. you probably want to wrap toolbars etc.).
Example:
```xml
<de.virality.enhancedcontentlayout.EnhancedContentLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:id="@+id/enahncedContent">

    ...

</de.virality.enhancedcontentlayout.EnhancedContentLayout>
```

### With direkt instantiation
If you want to wrap the complete layout (for example in a fragment) you can directly instantiate it. 
Example:
```java
@Override
public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

    enhancedContent = new EnhancedContentLayout(getContext(), R.layout.some_fragment, container);
    ...
    wrappedRecyclerView = (RecyclerView) enhancedContent.findViewById(R.id.recycler_view);
    ...
    enhancedContent.showProgress();
    ...
    return enhancedContent;
}
```

## Use the enhanced features

### Show a message
```
enhancedContent.showMessage(R.string.some_message);
enhancedContent.showMessage("Some message");
```
Useful for showing errors or empty messages (for lists). Hides the content and simply shows a message at the top of the layout.

### Show progress
```
enhancedContent.showProgress();
```
Hides the content and shows a progressBar in the center of the layout.

### Show content
```
enhancedContent.showContent();
```
Hides all other features (message, progressBar, ...) and shows the original content again (child views of the layout).

### Enable Swipe to refresh
```
enhancedContent.enableSwipeToRefresh(@NonNull OnRefreshListener listener);
```
Enables swipe to refresh on the layout and calls the onRefresh() method when the the user pulls down the content.

## ToDo
- Make message position customizable (e.g. allow to position it in the center)
- Allow to pass an optional String and OnClickListener to showMessage which then shows a button with the text and calls back on click. E.g. for error messages, this allows to add a "retry" button.
- More customizations (e.g. background color, images, ...)
- ...
