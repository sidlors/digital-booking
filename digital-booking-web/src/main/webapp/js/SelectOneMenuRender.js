PrimeFaces.widget.SelectOneMenu.prototype._renderBack = PrimeFaces.widget.SelectOneMenu.prototype._render;
PrimeFaces.widget.SelectOneMenu.prototype.ensureLabelWidth = function(){
	var me = this;
	var width = this.jq.width() - this.menuIcon.width();
	if(this.label.width() !== width){
		this.label.width(width);
		setTimeout(function(){me.ensureLabelWidth();}, 300);
	}
};
PrimeFaces.widget.SelectOneMenu.prototype._render = function(){
	this._renderBack();
	this.ensureLabelWidth();
};

PrimeFaces.widget.Dialog.prototype.initBack = PrimeFaces.widget.Dialog.prototype.init;
PrimeFaces.widget.Dialog.prototype.ensureContentPosition = function() {
	var me = this;
	var position = me.jq['0'].style.position;
	if (position === 'relative') {
		me.jq['0'].style.position = '';
		setTimeout(function() {
			me.ensureContentPosition();
		}, 300);
	}
};
PrimeFaces.widget.Dialog.prototype.init = function(a) {
	this.initBack(a);
//	if (!this.cfg.visible) {
//		this.ensureContentPosition();
//	}
};
PrimeFaces.widget.DataTable.prototype.paginate = function(d) {
    var b = {
        source : this.id,
        update : this.id,
        process : this.id,
        formId : this.cfg.formId
    };
    var message = PF('primefaceswaitingdlg');
    var c = this;
    b.onsuccess = function(j) {
        var g = $(j.documentElement), h = g.find("update");
        for (var e = 0; e < h.length; e++) {
            var l = h.eq(e), k = l.attr("id"), f = l.get(0).childNodes[0].nodeValue;
            if (k == c.id) {
                c.tbody.html(f);
                if (c.checkAllToggler) {
                    c.updateHeaderCheckbox();
                }
                if (c.cfg.scrollable) {
                    c.alignScrollBody();
                }
            } else {
                PrimeFaces.ajax.AjaxUtils.updateElement.call(this,
                        k, f);
            }
        }
        PrimeFaces.ajax.AjaxUtils.handleResponse.call(this, g);
        return true;
    };
    b.oncomplete = function() {
        c.paginator.cfg.page = d.page;
        c.paginator.updateUI();
        if(message){
            message.hide();
        }
    };
    b.params = [ {
        name : this.id + "_pagination",
        value : true
    }, {
        name : this.id + "_first",
        value : d.first
    }, {
        name : this.id + "_rows",
        value : d.rows
    }, {
        name : this.id + "_encodeFeature",
        value : true
    } ];
    
    if(message){
        message.show();
    }
    if (this.hasBehavior("page")) {
        var a = this.cfg.behaviors.page;
        a.call(this, d, b);
    } else {
        PrimeFaces.ajax.AjaxRequest(b);
    }
};


PrimeFaces.widget.SelectOneMenu.prototype.showBack = PrimeFaces.widget.SelectOneMenu.prototype.show;
PrimeFaces.widget.SelectOneMenu.prototype.show = function() {
    var a = this;
    this.showBack();
    if (a.cfg.filter) {
        a.filterInput.val('');
        a.items.filter(":hidden").show();
        if (a.cfg.initialHeight == 0 || a.itemsContainer.height() < a.cfg.initialHeight) {
            a.itemsWrapper.css("height", "auto");
        } else {
            a.itemsWrapper.height(a.cfg.initialHeight);
        }
    }
};
