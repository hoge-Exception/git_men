function showConfirmationDialog() {
    document.getElementById("dialog").style.display = "flex";
}

function confirmAction() {
    document.getElementById("registerForm").submit();
}

function cancelAction() {
    document.getElementById("dialog").style.display = "none";
}


        // function showConfirmationDialog() {
        //     if (confirm("ユーザー登録を実行しますか？")) {
        //         document.getElementById("registerForm").submit();
        //     }
        // }

        // // ダイアログのボタンラベルと配置を変更する
        // window.onload = function () {
        //     var originalConfirm = window.confirm;
        //     window.confirm = function (message) {
        //         var iframe = document.createElement("IFRAME");
        //         iframe.setAttribute("src", "data:text/plain,");
        //         document.documentElement.appendChild(iframe);

        //         var result = iframe.contentWindow.confirm(message);
        //         document.documentElement.removeChild(iframe);

        //         var dialog = document.querySelector("iframe");
        //         var confirmButton = dialog.contentDocument.querySelector('input[type="button"][value="OK"]');
        //         var cancelButton = dialog.contentDocument.querySelector('input[type="button"][value="キャンセル"]');
        //         confirmButton.value = "はい";
        //         cancelButton.value = "いいえ";
                
        //         // ボタンの配置を左右逆にする
        //         confirmButton.parentNode.insertBefore(cancelButton, confirmButton);
                
        //         return result;
        //     }
        // }
    