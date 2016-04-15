var DigitalBookingUtil = {};

DigitalBookingUtil.ns = function() {
	var len1 = arguments.length, i = 0, len2, j, main, ns, sub, current;

	for (; i < len1; ++i) {
		main = arguments[i];
		ns = arguments[i].split('.');
		current = window[ns[0]];
		if (current === undefined) {
			current = window[ns[0]] = {};
		}
		sub = ns.slice(1);
		len2 = sub.length;
		for (j = 0; j < len2; ++j) {
			current = current[sub[j]] = current[sub[j]] || {};
		}
	}
	return current;
};

DigitalBookingUtil.showErrorDialog = function(msg) {
    msg.detail = msg.detail.replace(/TAG_BR/g, '<br>');
    if( msg.detail.indexOf('<br>') != -1 ){
        msg.detail = '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' + msg.detail;
    }
    
	var messageDialog = PF("primefaceserrordlg");
		$(
				'<div id="primefaceserrordlg" class="ui-message-dialog ui-dialog ui-widget ui-widget-content ui-corner-all ui-shadow ui-overlay-hidden"/>')
				.append(
						'<div class="ui-dialog-titlebar ui-widget-header ui-helper-clearfix ui-corner-top"><span class="ui-dialog-title"></span><a class="ui-dialog-titlebar-icon ui-dialog-titlebar-close ui-corner-all" href="#" role="button"><span class="ui-icon ui-icon-closethick"></span></a></div><div class="ui-dialog-content ui-widget-content" style="height: auto;"></div>')
				.appendTo(document.body);
		PrimeFaces.cw("Dialog", "primefaceserrordlg", {
			id : "primefaceserrordlg",
			modal : true,
			draggable : true,
			resizable : false,
			showEffect : "fade",
			hideEffect : "fade",
			onShow : function(){ $('#messageErrorBtn').focus(); }
		});
		messageDialog = PF("primefaceserrordlg");
		messageDialog.titleContainer = messageDialog.titlebar
				.children("span.ui-dialog-title");
		messageDialog.content
				.html("")
				.append(msg.detail)
				.append(
						'<span /><br><br><div style="text-align:center"><input type="button" id="messageErrorBtn" value="Ok"  class="ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only"/></div>');
		var closeDialog = function() {
			PF('primefaceserrordlg').hide();
		};
		$('#messageErrorBtn').click(closeDialog);
	messageDialog.titleContainer.text(msg.summary);
	messageDialog.content.find('span').removeClass().addClass(
			'ui-dialog-message ui-messages-'
					+ msg.severity.split(" ")[0].toLowerCase() + '-icon');
	messageDialog.show();

	setInterval(function() {
		$('#messageErrorBtn').focus();
	}, 500);

};

DigitalBookingUtil.extractSourceFromArgs = function(c) {
	var regex = /&javax\.faces\.source=/;
	var index = c.data.search(regex) + 1;
	var infoSource = c.data.substring(index, c.data.indexOf('&', index));
	return infoSource.replace(/%3A/g, ':').replace('javax.faces.source=', '');
};

DigitalBookingUtil.showMessageInDialog = function(cfg) {
	var messageDialog = PF("primefacesmessagedialog");
	if (!messageDialog) {
		DigitalBookingUtil.ns('cinepolis.msg.successbuttons');
		$(
				'<div id="primefacesmessagedlg" class="ui-message-dialog ui-dialog ui-widget ui-widget-content ui-corner-all ui-shadow ui-overlay-hidden"/>')
				.append(
						'<div class="ui-dialog-titlebar ui-widget-header ui-helper-clearfix ui-corner-top"><span class="ui-dialog-title"></span></div><div class="ui-dialog-content ui-widget-content" style="height: auto;"></div>')
				.appendTo(document.body);
		PrimeFaces.cw("Dialog", "primefacesmessagedialog", {
			id : "primefacesmessagedlg",
			modal : true,
			draggable : false,
			resizable : false,
			closable : false,
			showEffect : "fade",
			hideEffect : "fade",
			onShow : function(){ $('#messageSuccessBtn').focus();}
		});
		messageDialog = PF("primefacesmessagedialog");
		messageDialog.titleContainer = messageDialog.titlebar
				.children("span.ui-dialog-title");
		messageDialog.titleContainer.text(cfg.summary);
		messageDialog.content
				.html("")
				.append(cfg.detail)
				.append(
						'<span class="ui-dialog-message ui-messages-info'
								+ '-icon" /><br><br><div style="text-align:center"><input type="button" id="messageSuccessBtn" value="Ok" class="ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only"/></div>');
		
		var closeDialog = function() {
			var dialog = PF('primefacesmessagedialog');
			if (!dialog.url) {
				dialog.hide();
			} else {
				document.location = CONTEXT_APP + dialog.url;
			}
		};

		applyFocus = function() {
			this.jq.find(":button,:submit").filter(":visible:enabled").eq(0)
					.focus();
		};
		$('#messageSuccessBtn').click(closeDialog);

		$(document).on('pfAjaxComplete', function(a, b, c) {
			if (!c.args || (!c.args.validationFailed && !c.args.notValid)) {
				var infoSource = DigitalBookingUtil.extractSourceFromArgs(c);
				if (cinepolis.msg.successbuttons.hasOwnProperty(infoSource)) {
					var dialog = PF('primefacesmessagedialog');
					dialog.url = cinepolis.msg.successbuttons[infoSource];
					dialog.show();
					
				}
			}
		});
	}
	cinepolis.msg.successbuttons[cfg.source] = cfg.url;
};