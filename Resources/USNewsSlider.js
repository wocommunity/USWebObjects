var NewsSlider = Class.create();
NewsSlider.prototype = {

	initialize: function(interval, fadeDuration, stopText, prevText, nextText)
	{
		this.interval = interval;
		this.fadeDuration = fadeDuration;
		this.stopText = stopText;
		this.prevText = prevText;
		this.nextText = nextText;
		
		this.timeout = null;
		this.loopTimeout = null;
		this.current_message = null;
		this.previous_message = null;
		
		this.container = $("slideContent");
		this.messages  = $A(this.container.getElementsByTagName("li"));
		this.number_of_messages = this.messages.length;
		if (this.number_of_messages == 0)
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
  	buildIndex: function() {
  		container = $("slideIndex");
  		
  		for (i = 0; i < this.number_of_messages; i++) {
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
  	addStopButton: function() {
  		container = $("slideStopButton");
  		
  		link = document.createElement("a");
  		link.href = "#";
  		link.id = "stop";
  		link.innerHTML = this.stopText;
  		Event.observe(link, "click", this.stopSlideShow.bind(this), false);
  		container.appendChild(link);
  	},
  	stopSlideShow: function() {
  		clearTimeout(this.loopTimeout);
  		clearTimeout(this.timeout);
  	},
	showNextMessage: function()
	{
		if (this.current_message == null) {
			this.showMessageNumber(0);
		}
		else {
			this.showMessageNumber(this.current_message + 1);
		}
	},
	showMessageNumber: function(index) {
		this.setMessageIndexes(index);
		this.timeout = setTimeout(this.fadeMessageOut.bind(this), (this.interval - this.fadeDuration) * 1000);
		this.fadeMessageIn();
		this.loopTimeout = setTimeout(this.showNextMessage.bind(this), this.interval * 1000);
	},
	showMessageNumberInstantly: function(index) {
		clearTimeout(this.timeout);
		clearTimeout(this.loopTimeout);
		this.fadeMessageOut();		
		this.loopTimeout = setTimeout(this.showMessageNumber.bind(this, index), this.fadeDuration * 1000);
	},
	showNextMessageInstantly: function() {
		if (this.current_message == null) {
			this.showMessageNumberInstantly(0);
		}
		else {
			this.showMessageNumberInstantly(this.current_message + 1);
		}
	},
	showPreviousMessageInstantly: function() {
		if (this.current_message == null) {
			this.showMessageNumberInstantly(0);
		}
		else if (this.current_message == 0 ) {
			this.showMessageNumberInstantly(this.number_of_messages - 1);
		}
		else {
			this.showMessageNumberInstantly(this.current_message - 1);
		}
	},
	setMessageIndexes: function(index) {
		this.previous_message = this.current_message;
		
		if (index >= this.number_of_messages)
		{			
			this.current_message = 0;
		} 
		else {
			this.current_message = index;
		}			
		this.setIndexNumberStyle();
	},
	setIndexNumberStyle: function() {
		this.messageIndexes.each(function(messageIndex)
		{
			Element.removeClassName(messageIndex, 'slideIndexActive');
			Element.addClassName(messageIndex, 'slideIndexInActive');
		});
		Element.removeClassName(this.messageIndexes[this.current_message], 'slideIndexInActive');
		Element.addClassName(this.messageIndexes[this.current_message], 'slideIndexActive');
	},
	fadeMessageOut: function()
	{
		Effect.Fade(this.messages[this.current_message], {duration: this.fadeDuration});
	},
	fadeMessageIn: function()
	{
		Effect.Appear(this.messages[this.current_message], {duration: this.fadeDuration});
	},
	hideMessages: function()
	{
		this.messages.each(function(message)
		{
			Element.hide(message);
		})
	},
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