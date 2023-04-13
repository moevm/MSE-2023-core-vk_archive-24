package ui.mainWindow

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf

class CheckboxListManager {
    private val mapOfCheckboxesState = mutableMapOf<String, MutableState<Boolean>>()
    private val setOfCheckboxesId = mutableSetOf<String>()

    private val _showCheckboxes = mutableStateOf(false)
    val showCheckboxes: State<Boolean> = _showCheckboxes

    fun createCheckboxState(id: String) {
        if(!mapOfCheckboxesState.containsKey(id))
            mapOfCheckboxesState[id] = mutableStateOf(false)
    }

    fun updateCheckboxState(id: String, state: Boolean) {
        if (mapOfCheckboxesState.containsKey(id)) {
            mapOfCheckboxesState[id]!!.value = state
            if (state)
                setOfCheckboxesId.add(id)
            else
                setOfCheckboxesId.remove(id)

            _showCheckboxes.value = setOfCheckboxesId.isNotEmpty()
            if (!_showCheckboxes.value)
                hideCheckbox()
        }
    }

    fun getCheckboxState(id: String): State<Boolean> {
        if (mapOfCheckboxesState.containsKey(id)) {
            return mapOfCheckboxesState[id]!!
        } else throw IllegalArgumentException("Invalid id for checkbox state")
    }

    fun getSelectedIds() = setOfCheckboxesId

    fun selectAllCheckboxes() {
        for ((id, _) in mapOfCheckboxesState) {
            mapOfCheckboxesState[id]!!.value = true
            setOfCheckboxesId.add(id)
        }
        _showCheckboxes.value = true
    }

    fun hideCheckbox() {
        for (id in setOfCheckboxesId) {
            mapOfCheckboxesState[id]?.value = false
        }
        setOfCheckboxesId.clear()
        _showCheckboxes.value = false
    }
}
