function NoteItem(props) {
    const {id, text, removeNote = Function.prototype} = props;

    return (
        <div className='card'>
            <div>
                <span className="secondary-content" onClick={() => removeNote(id)}>
                    <i className="material-icons note-delete">close</i>
                </span>
            </div>
            <div className='card-content'>
                <p>{text.slice(0, 160)} {text.length > 160 ? '...' : ''}</p>
            </div>
            <div className='card-action'>
                <a href={`/note/find-one/${id}`} className='btn'>
                    Watch note
                </a>
            </div>
        </div>
    );
}

export {NoteItem};