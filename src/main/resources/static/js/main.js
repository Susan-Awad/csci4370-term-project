
// Make sure the dom is loaded.
document.addEventListener('DOMContentLoaded', function () {
    // submittable is expected to be text fields.
    var submittables = document.getElementsByClassName('submittable');

    for (var submittable of submittables) {
        // Make text field submit the enclosing form when the enter key is pressed.
        submittable.addEventListener('keydown', function (e) {
            // Check if Enter was pressed without the Shift key
            if (e.key === 'Enter' && !e.shiftKey) {
                e.preventDefault(); // Prevent new lines.
                this.form.submit(); // Submit the form.
                console.log(this.form + ' was submitted.');
            }
        });
    }
});

function editCard(button) {
    const card = button.closest(".budget-item-view")
    const editCard = card.nextElementSibling;

    card.style.display = 'none';
    editCard.style.display = 'block';
}

function cancelCard(button) {
    const editCard = button.closest(".budget-item-edit");
    const viewCard = editCard.previousElementSibling;

    editCard.style.display = 'none';
    viewCard.style.display = 'block';
}