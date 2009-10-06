/*  
* Accessible news slide show
*
* Prototype/scriptaculous javascript to display a news slide show.
* The content is accessible even though javascript/css is not supported
*
* Author: Atli Páll Hafsteinsson <atlip@us.is>
* Based on "Accessible JavaScript Newsticker" by Bartelme Design
* http://www.bartelme.at/journal/archive/accessible_javascript_newsticker
*/
var NewsSlider = Class.create();
NewsSlider.prototype = {
	
	/* 
	* Initializes all global vars and gets the slides from the html
	* interval: The time each slide is displayed in seconds
	* fadeDuration: the duration of the fade effect in seconds
	* stopText: the text on the stop button
	* prevText: the text on the previous slide button
	* nextText: the text on the next slide button
	*/
	initialize: function(interval, fadeDuration, stopText, prevText, nextText)
	{
		this.interval = interval;
		this.fadeDuration = fadeDuration;
		this.stopText = stopText;
		this.prevText = prevText;
		this.nextText = nextText;
		
		this.timeout = null;
		this.loopTimeout = null;
		this.currentMessage = null;
		
		this.container = $("slideContent");
		this.messages  = $A(this.container.getElementsByTagName("li"));
		this.numberOfMessages = this.messages.length;
		if (this.numberOfMessages == 0)
		{
			this.showError();
			return false;
		}
		
		this.buildIndex();
		this.addPreviousNextButtons();
		this.addStopButton();
		this.messageIndexes = $A($("slideIndex").getElementsByTagName("a"));
	
		this.hideMessages();
		this.showNextMessage();
  	},
  	/* 
  	* Builds the index links 
  	*/
  	buildIndex: function() {
  		container = $("slideIndex");
  		
  		for (i = 0; i < this.numberOfMessages; i++) {
  			span = document.createElement("span");
  			link = document.createElement("a");
  			link.href = "#";
  			link.id = "index_" + i;
  			link.innerHTML = i + 1;
  			Event.observe(link, "click", this.showMessageNumberInstantly.bind(this, i), false);
  			
  			span.appendChild(link);
  			container.appendChild(span);
  		}  		
  	},
  	/* 
  	* Adds the previous / next buttons
  	*/
  	addPreviousNextButtons: function() {
  		container = $("slidePreviousNextButtons");
  		
  		prev = document.createElement("a");
  		prev.href = "#";
  		prev.id = "prev";
  		prev.innerHTML = this.prevText;
  		Event.observe(prev, "click", this.showPreviousMessageInstantly.bind(this), false);
  		container.appendChild(prev);
  			
  		next = document.createElement("a");
  		next.href = "#";
  		next.id = "next";
  		next.innerHTML = this.nextText;
  		Event.observe(next, "click", this.showNextMessageInstantly.bind(this), false);
  		container.appendChild(next);
  	},
  	/* 
  	* Adds the stop button
  	*/
  	addStopButton: function() {
  		container = $("slideStopButton");
  		
  		link = document.createElement("a");
  		link.href = "#";
  		link.id = "stop";
  		link.innerHTML = this.stopText;
  		Event.observe(link, "click", this.stopSlideShow.bind(this), false);
  		container.appendChild(link);
  	},
  	/* 
  	* Stops the slide show
  	*/
  	stopSlideShow: function() {
  		clearTimeout(this.loopTimeout);
  		clearTimeout(this.timeout);
  	},
  	/* 
  	* Displays the next message
  	*/
	showNextMessage: function()
	{
		if (this.currentMessage == null) {
			this.showMessageNumber(0);
		}
		else {
			this.showMessageNumber(this.currentMessage + 1);
		}
	},
	/* 
	* Displays the slide with the given index
	* index: The index of the slide to display 
	*/
	showMessageNumber: function(index) {
		this.setMessageIndexes(index);
		this.timeout = setTimeout(this.fadeMessageOut.bind(this), (this.interval - this.fadeDuration) * 1000);
		this.fadeMessageIn();
		this.loopTimeout = setTimeout(this.showNextMessage.bind(this), this.interval * 1000);
	},
	/* 
	* Shows the slide with the given index instantly (with fade effect)
	* index: The index of the slide to display
	*/
	showMessageNumberInstantly: function(index) {
		clearTimeout(this.timeout);
		clearTimeout(this.loopTimeout);
		this.fadeMessageOut();		
		this.loopTimeout = setTimeout(this.showMessageNumber.bind(this, index), this.fadeDuration * 1000);
	},
	/* 
	* Shows the next slide instantly (with fade effect)
	*/
	showNextMessageInstantly: function() {
		if (this.currentMessage == null) {
			this.showMessageNumberInstantly(0);
		}
		else {
			this.showMessageNumberInstantly(this.currentMessage + 1);
		}
	},
	/* 
	* Shows the previous slide instantly (with fade effect)
	*/
	showPreviousMessageInstantly: function() {
		if (this.currentMessage == null) {
			this.showMessageNumberInstantly(0);
		}
		else if (this.currentMessage == 0 ) {
			this.showMessageNumberInstantly(this.numberOfMessages - 1);
		}
		else {
			this.showMessageNumberInstantly(this.currentMessage - 1);
		}
	},
	/* 
	* Sets the current slide index to the given index
	* index: The index number to set the current slide index to
	*/
	setMessageIndexes: function(index) {
		
		if (index >= this.numberOfMessages)
		{			
			this.currentMessage = 0;
		} 
		else {
			this.currentMessage = index;
		}			
		this.setIndexNumberStyle();
	},
	/* 
	* Sets the styles for the slide index indicator
	*/
	setIndexNumberStyle: function() {
		this.messageIndexes.each(function(messageIndex)
		{
			Element.removeClassName(messageIndex, 'slideIndexActive');
			Element.addClassName(messageIndex, 'slideIndexInActive');
		});
		Element.removeClassName(this.messageIndexes[this.currentMessage], 'slideIndexInActive');
		Element.addClassName(this.messageIndexes[this.currentMessage], 'slideIndexActive');
	},
	/* 
	* Fades out the current slide 
	*/
	fadeMessageOut: function()
	{
		Effect.Fade(this.messages[this.currentMessage], {duration: this.fadeDuration});
	},
	/* 
	* Fades oin the current slide
	*/
	fadeMessageIn: function()
	{
		Effect.Appear(this.messages[this.currentMessage], {duration: this.fadeDuration});
	},
	/* 
	* Hides all the slides
	*/
	hideMessages: function()
	{
		this.messages.each(function(message)
		{
			Element.hide(message);
		})
	},
	/* 
	* Displays en error message
	*/
	showError: function()
	{
		if (this.container.getElementsByTagName("ul").length == 0)
		{
			this.list = document.createElement("ul");
			this.container.appendChild(this.list);
		} else {
			this.list = this.container.getElementsByTagName("ul")[0];
		}
		this.errorMessage = document.createElement("li");
		this.errorMessage.className = "error";
		this.errorMessage.innerHTML = "Could not retrieve data";
		this.list.appendChild(this.errorMessage);
	}
}