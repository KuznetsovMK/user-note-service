import {useState} from "react";
import {createNote} from "../api";

function NewNote() {
    const [noteData, setNoteData] = useState({id: localStorage.getItem('clientUserId'), text: ''});

    const onChange = ({target}) => {
        setNoteData({...noteData, [target.name]: target.value})
    }

    return (
        <div>
            <div className="textarea">
                <textarea required="required"
                          onChange={onChange}
                          name='text'
                          value={noteData.text}/>
                <div className="placeholder">Введите текст</div>

            </div>
            <div>
                <a
                    href={`/`}
                    className="waves-effect waves-light btn"
                    onClick={() => createNote(noteData)}
                >Create</a>
            </div>
        </div>
    );
}

export {NewNote};