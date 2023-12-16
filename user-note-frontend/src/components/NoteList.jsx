import {NoteItem} from "./NoteItem";

function NoteList({notes, removeNote = Function.prototype}) {

    return (
        <div className='list'>
            <a href={`/note/new-note`} className="btn-floating btn-large waves-effect waves-light green"><i
                className="material-icons">add</i></a>
            {notes.map((note) => (
                <NoteItem key={note.id} {...note} removeNote={removeNote}/>
            ))}
        </div>
    );
}

export {NoteList};