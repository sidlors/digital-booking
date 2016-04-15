PrimeFaces.widget.WaitingDialog = PrimeFaces.widget.BaseWidget
        .extend({
            init : function(a) {
                this._super(a);
                this._render();
                this.extractWidgets();
                if (this.components && this.components.length > 0) {
                    this.bindEvents();
                }
            },

            _render : function() {
                $(
                        '<div id="primefaceswaitingdlg" class="ui-message-dialog ui-dialog ui-widget ui-widget-content ui-corner-all ui-shadow ui-overlay-hidden"/>')
                        .append(
                                '<div class="ui-dialog-titlebar ui-widget-header ui-helper-clearfix ui-corner-top"><span class="ui-dialog-title"></span></div><div class="ui-dialog-content ui-widget-content" style="height: auto;"></div>')
                        .appendTo(document.body);
                PrimeFaces.cw("Dialog", "primefaceswaitingdlg", {
                    id : "primefaceswaitingdlg",
                    modal : true,
                    draggable : false,
                    resizable : false,
                    closable : false,
                    showEffect : "fade",
                    hideEffect : "fade"
                });
                var messageDialog = PF("primefaceswaitingdlg");
                messageDialog.titleContainer = messageDialog.titlebar.children("span.ui-dialog-title");
                messageDialog.titleContainer.text(this.cfg.title);
                messageDialog.content.html("").append(
                        '<img alt="" src="' + CONTEXT_APP + '/images/ajaxloadingbar.gif">');
            },

            extractWidgets : function() {
                var components = this.cfg.components.replace(/[,][\s]{0,}/g, ',');
                this.components = components.split(',');
                for (var i = 0; i < this.components.length; i++) {
                    var component = this.components[i];
                    if (component.indexOf(':') === 0) {
                        component = component.substr(1);
                    }
                    this.components[i] = eval('/^' + component + '$/');
                }
            },

            bindEvents : function() {
                $(document).on('pfAjaxSend', null, this, this.evalShowWaitingDlg).on('pfAjaxSuccess', null, this,
                        this.enableEvents).on('pfAjaxComplete', null, this, this.evalHideWaitingDlg);
            },

            findComponent : function(source) {
                var result = -1;
                for (var i = 0; i < this.components.length; i++) {
                    var component = this.components[i];
                    if (component.test(source)) {
                        result = i;
                    }
                }
                return result;
            },

            evalShowWaitingDlg : function(a, b, c) {
                var me = a.data;
                var infoSource = DigitalBookingUtil.extractSourceFromArgs(c);
                var index = me.findComponent.call(me, infoSource);
                if (index !== -1) {
                    PF("primefaceswaitingdlg").show();
                }
            },

            enableEvents : function(a, b, c) {
                var me = a.data;
                var infoSource = DigitalBookingUtil.extractSourceFromArgs(c);
                var index = me.findComponent.call(me, infoSource);
                if (index !== -1) {
                    $(document).unbind(PF("primefaceswaitingdlg").blockEvents).unbind("keydown.modal-dialog");
                }
            },

            evalHideWaitingDlg : function(a, b, c) {
                var me = a.data;
                var infoSource = DigitalBookingUtil.extractSourceFromArgs(c);
                var index = me.findComponent.call(me, infoSource);
                if (index !== -1) {
                    PF("primefaceswaitingdlg").hide();
                }
            }
        });