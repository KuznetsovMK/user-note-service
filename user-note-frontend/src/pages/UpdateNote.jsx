import {useParams} from "react-router-dom";
import {useEffect, useState} from "react";
import {getNoteById, updateNote} from "../api";


function UpdateNote() {
    const {id} = useParams();
    const [noteData, setNoteData] = useState({
        clientId: '',
        noteId: '',
        text: ''
    });

    const onChange = ({target}) => {
        setNoteData({...noteData, [target.name]: target.value})
    }

    useEffect(() => {
        getNoteById(id).then((data) => {
            setNoteData({clientId: localStorage.getItem('clientUserId'), text: data.text, noteId: data.id});
        })
    }, []);

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
                    onClick={() => updateNote(noteData)}
                >Save</a>
            </div>
        </div>
    );
}

export {UpdateNote};