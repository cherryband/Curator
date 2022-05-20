# Curator
Powerful yet intuitive gallery management app for Android.

# Features
Curator looks like a standard gallery app, but it's not for viewing photos.
Curator is in essence a many-to-many mass file mover.
Select folders you want to view, select images to move, and choose destinations.
All selections are tagged, which means you can move each files to a different destination to each other in single pass.

The features do not end there, however.
Planned roadmap includes folder tagging, autotagging based on parameters such as directory path, name, modified time, etc.,
IETF tagging(embed tags into image file, readable on any supported image readers), sorting by colour, and auto move based on autotagging in the background.

# Why?
I have a large library of photos, and managing them is, almost always, time consuming.
Normal gallery apps only show photo library in shallow "albums", which is not only **cumbersome to navigate**,
but ᅟalso **generates ambiguity** ("2015/May" and "2017/May" will both simply appear as *"May"* in many apps).

Most gallery apps also have limited move options.
You often have access to **only 1 folder, 1 selection option, and 1 destination.**
If you have a folder with only 3 different destinations, that makes it at least 3 pass of multiple selection and moving operations.
This is again multiplied if you have more folders to select from.
And copy/move is often *not* displayed as a prominent option in the app, unlike delete and select all.

Most desktop programs have limitation too, namely that you can only see photos in **isolation** or in **small thumbnails**.
Such limitation makes mass operation practical only through text (e.g., file name, modified date, file type).
Making **non-linear selections** is also difficult, often requiring both keyboard and mouse actions.

And last but not least, directory-based management can only be assigned **a single property**,
whereas tag-based management can assign *multiple properties* to a single element.
Almost no Android app supports IETF tag reading, and no app supports batch tagging.

These are all motivations for the development, which is personal but I believe will fill a niche for mass tagging and/or mass moving photos in smartphones.
