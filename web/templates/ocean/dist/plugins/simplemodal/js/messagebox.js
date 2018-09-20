var MessageBox = {
    container: null,
    callback: null,
    show: function (message, buttons, icons, callback, owner) {
        var self = this;
        
        if ($("#osx-modal-content").length == 0) {
            $(document.body).append('<style>#osx-modal-content p{text-align: center;} #osx-modal-content #osx-modal-data-btns button{margin:0 10px}</style>');
            $(document.body).append('<div id="osx-modal-content"><div id="osx-modal-data"><p id="osx-modal-data-msg"></p><p id="osx-modal-data-btns"><button class="btn yes simplemodal-close btn-primary" id="osx-modal-data-btns-ok"><span class="icon-comment-fill"></span>确定</button><button class="btn no simplemodal-close btn-primary" id="osx-modal-data-btns-cancel"><span class="icon-comment-fill"></span>取消</button></p></div></div></div>');
        }
        self.callback = callback;
        self._buildButtons(buttons);
        $("#osx-modal-data-msg").html(message);
        $("#osx-modal-content").modal({
            overlayId: 'osx-overlay',
            containerId: 'osx-container',
            closeHTML: null,
            minHeight: 80,
            opacity: 65,
            position: [50,0],
            overlayClose: false,
            onShow: self._show,
            onOpen: self._open,
            onClose: self._close
        });
    }, // end show
    _show: function (d) {
        $('.yes', d.data[0]).click(function () {
            if (MessageBox.callback != null) {
                MessageBox.callback();
            }
        });
    }, // end _show
    _buildButtons: function (buttons) {
        $("#osx-modal-data-btns button").each(function (i) { this.style.display = "none"; })
        //var button;
        if (buttons & MessageBoxButtons.OK) {
            $("#osx-modal-data-btns-ok").css("display", "");
        }
        if (buttons & MessageBoxButtons.Cancel) {
            $("#osx-modal-data-btns-cancel").css("display", "");
        }
        if (buttons & MessageBoxButtons.Abort) {

        }
    }, // end buildButtons
    _open: function (d) {
        var self = this;
        self.container = d.container[0];
        d.overlay.fadeIn('slow', function () {
            $("#osx-modal-content", self.container).show();
            d.container.slideDown('slow', function () {
                setTimeout(function () {
                    var h = $("#osx-modal-data", self.container).height() + 20; d.container.animate({ height: h }, 200, function () { $("#osx-modal-data", self.container).show(); }
                    );
                }, 300);
            });
        });

    }, // end _open;
    _close: function (d) {
        var self = this; // this = SimpleModal object
        d.container.animate({ top: "-" + (d.container.height() + 20) }, 500, function () { self.close(); });
    } // end _close;
} // end MessageBox

var MessageBoxButtons = {
    OK: 1,
    Cancel: 2,
    Abort: 4,
    OKCancel: 3,
    AbortOKCancel: 7
} // end MessageBoxButtons

var DialogResult = {
    Cancel: 0,
    OK: 1
}// end DialogResult