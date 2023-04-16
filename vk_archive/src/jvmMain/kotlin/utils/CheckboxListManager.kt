package utils

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf

interface CheckboxListManager {
    val mapOfCheckboxesState: MutableMap<String, MutableState<Boolean>>
    val setOfCheckboxesId: MutableSet<String>
    fun createCheckboxState(id: String) {
        if(!checkboxExists(id))
            mapOfCheckboxesState[id] = mutableStateOf(false)
    }

    fun createCheckboxStates(ids: List<String>) {
        ids.forEach { id -> createCheckboxState(id) }
    }

    fun updateCheckboxState(id: String, state: Boolean) {
        if (checkboxExists(id)) {
            mapOfCheckboxesState[id]!!.value = state
            if (state)
                setOfCheckboxesId.add(id)
            else
                setOfCheckboxesId.remove(id)
        }
    }

    fun getCheckboxState(id: String): State<Boolean> {
        if (checkboxExists(id)) {
            return mapOfCheckboxesState[id]!!
        } else throw IllegalArgumentException("Invalid id for checkbox state")
    }

    fun checkboxExists(id: String) = mapOfCheckboxesState.containsKey(id)

    fun getSelectedIds() = setOfCheckboxesId

    fun selectAllCheckboxes() {
        for ((id, _) in mapOfCheckboxesState) {
            mapOfCheckboxesState[id]!!.value = true
            setOfCheckboxesId.add(id)
        }
    }

    fun unselectAllCheckboxes() {
        for (id in setOfCheckboxesId) {
            mapOfCheckboxesState[id]?.value = false
        }
        setOfCheckboxesId.clear()
    }
}

class DefaultCheckboxListManager : CheckboxListManager {
    override val mapOfCheckboxesState = mutableMapOf<String, MutableState<Boolean>>()
    override val setOfCheckboxesId = mutableSetOf<String>()
}

class HideableCheckboxListManager : CheckboxListManager {
    override val mapOfCheckboxesState = mutableMapOf<String, MutableState<Boolean>>()
    override val setOfCheckboxesId = mutableSetOf<String>()

    private val _showCheckboxes = mutableStateOf(false)
    val showCheckboxes: State<Boolean> = _showCheckboxes

    override fun updateCheckboxState(id: String, state: Boolean) {
        super.updateCheckboxState(id, state)
        if (checkboxExists(id)) {
            _showCheckboxes.value = setOfCheckboxesId.isNotEmpty()
            if (!_showCheckboxes.value) hideCheckbox()
        }
    }

    override fun selectAllCheckboxes() {
        super.selectAllCheckboxes()
        _showCheckboxes.value = true
    }

    fun hideCheckbox() {
        super.unselectAllCheckboxes()
        _showCheckboxes.value = false
    }
}
