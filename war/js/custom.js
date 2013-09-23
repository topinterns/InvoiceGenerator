/* Slider 1 - Parallax slider*/
jQuery(function() {
	jQuery('#da-slider').cslider({
		autoplay	: true,
		interval : 9000
	});
});
	
/* Flex slider */

  jQuery(window).load(function() { 
	  
	      jQuery('.flexslider').flexslider({
	    	  easing: "easeInOutSine",
	          directionNav: false,
	          animationSpeed: 1500,
	          slideshowSpeed: 5000
	       
	  
	      });
	  
	  });


/* Image block effects */

  jQuery(function() {
	  jQuery('ul.hover-block li').hover(function(){
		  jQuery(this).find('.hover-content').animate({top:'-3px'},{queue:false,duration:500});
      }, function(){
    	  jQuery(this).find('.hover-content').animate({top:'125px'},{queue:false,duration:500});
      });
});

/* Slide up & Down */

  jQuery(".discover a").click(function(e){
	e.preventDefault();
	var myClass=$(this).attr("id");
	jQuery(".dis-content ."+myClass).toggle('slow');
});


/* Image slideshow */

jQuery('#s1').cycle({ 
    fx:    'fade', 
    speed:  2000,
    timeout: 300,
    pause: 1
 });

/* Support */

jQuery("#slist a").click(function(e){
   e.preventDefault();
   jQuery(this).next('p').toggle(200);
});

/* prettyPhoto Gallery */

jQuery("a[class^='prettyPhoto']").prettyPhoto({
overlay_gallery: false, social_tools: false
});


/* Isotype */

// cache container
var container = jQuery('#portfolio');
// initialize isotope
container.isotope({
  // options...
});

// filter items when filter link is clicked
jQuery('#filters a').click(function(){
  var selector = jQuery(this).attr('data-filter');
  $container.isotope({ filter: selector });
  return false;
});

