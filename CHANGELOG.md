Change Log
=======================================

Version 1.0.1 *(2015-11-11)*
----------------------------

 * Add some methods and attributes:
 	* Remove 'background_color' attribute with color reference. Now you can use the new 'label_background_res' attribute. It can be a color or a drawable. It's a reference.
 	* Remove 'setBackgroundColor' method. Now you can use the new 'setBackgroundResource' method. You can use a color or a drawable.
 	* Remove 'withBackgrondColor' Builder method. Now you can use the new 'withBackgrondResource' Builder method. You can use a color or a drawable.
 	* Added 'label_padding' attribute, 'setLabelPadding' method and 'withLabelPadding' Builder method.
 	* Constructors refactoring to new attributes.
 	* Demo modified to use the new methods and attributes.
 	* Public some methods. Issue: (https://github.com/DavidPizarro/AutoLabelUI/issues/7)


Version 1.0 *(2015-06-18)*
----------------------------
Initial release.
