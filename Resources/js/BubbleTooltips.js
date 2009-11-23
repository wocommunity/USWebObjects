/*javascript for Bubble Tooltips by Alessandro Fulciniti
- http://pro.html.it - http://web-graphics.com */

var bubbleTooltipsLoaded = true;


function enableTooltips( id, tag ) {
	var links,i,h;

	if( !document.getElementById || !document.getElementsByTagName )
		return;

	h=document.createElement("span");
	h.id="btc";
	h.setAttribute("id","btc");
	h.style.position="absolute";
	
	/* IE wont like this anymore */
	Event.observe(window, 'load', function() { 
	 document.getElementsByTagName("body")[0].appendChild(h);
	});
	
	if(id==null) {
	   if(tag==null) 
	        links=document.getElementsByTagName("a");
	   else links=document.getElementsByTagName(tag);
	}
	else {
	    if(tag==null) {
	        links=document.getElementById(id).getElementsByTagName("a");
	    }
	    else {
	        links=document.getElementById(id).getElementsByTagName(tag);
	    }
	}

	for(i=0;i<links.length;i++){
	    Prepare(links[i]);
    }
}

function Prepare(el){
	var tooltip,t,b,s;
	t=el.getAttribute("title");

	if( t!=null && t.length > 0 ) {
		el.removeAttribute("title");
		tooltip=CreateEl("span","tooltip");
		s=CreateEl("span","top");
		s.appendChild(document.createTextNode(t));
		tooltip.appendChild(s);
		b=CreateEl("b","bottom");
		tooltip.appendChild(b);
		setOpacity(tooltip);
		el.tooltip=tooltip;
		el.onmouseover=showTooltip;
		el.onmouseout=hideTooltip;
		el.onmousemove=Locate;
	}
}

function showTooltip(e){
	document.getElementById("btc").appendChild(this.tooltip);
	Locate(e);
}

function hideTooltip(e){
	var d=document.getElementById("btc");
	if(d.childNodes.length>0) 
		d.removeChild(d.firstChild);
}

function setOpacity(el){
	el.style.filter="alpha(opacity:90)";
	el.style.KHTMLOpacity="0.90";
	el.style.MozOpacity="0.90";
	el.style.opacity="0.90";
}

function CreateEl(t,c){
	var x=document.createElement(t);
	x.className=c;
	x.style.display="block";
	return(x);
}

function Locate(e){
	var posx=0,posy=0;
	if(e==null) e=window.event;
	if(e.pageX || e.pageY){
	    posx=e.pageX; posy=e.pageY;
	    }
	else if(e.clientX || e.clientY){
	    if(document.documentElement.scrollTop){
	        posx=e.clientX+document.documentElement.scrollLeft;
	        posy=e.clientY+document.documentElement.scrollTop;
	        }
	    else{
	        posx=e.clientX+document.body.scrollLeft;
	        posy=e.clientY+document.body.scrollTop;
	        }
	    }
	document.getElementById("btc").style.top=(posy+6)+"px";
	document.getElementById("btc").style.left=(posx-44)+"px";
}