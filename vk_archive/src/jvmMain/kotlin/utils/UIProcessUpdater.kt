package utils

import ui.alertDialog.AlertDialogWithProcessState

interface UIProcessUpdater {
    fun initProcess()

    fun updateProcessStatus(process: String)

    fun finishProcess()
}

abstract class SimpleAlertDialogProcessUpdater(
    private val alertDialogWithProcessState: AlertDialogWithProcessState
) : UIProcessUpdater {
    override fun initProcess() {
        alertDialogWithProcessState.status.value = ""
        alertDialogWithProcessState.isShow.value = true
    }

    override fun updateProcessStatus(process: String) {
        alertDialogWithProcessState.status.value = process
    }

    override fun finishProcess() {
        alertDialogWithProcessState.isShow.value = false
        alertDialogWithProcessState.processText.value = ""
        alertDialogWithProcessState.status.value = ""
    }
}