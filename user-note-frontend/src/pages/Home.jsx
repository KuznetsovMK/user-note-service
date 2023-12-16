import {useEffect, useState} from 'react';
import {deleteNoteById, getAllMyNotes, loginUser, registrationUser} from '../api';
import {NoteList} from "../components/NoteList";

function Home() {
    const [notes, setNotes] = useState([]);
    const [clientId] = useState('10000000-0000-0000-0000-000000000001');
    const [login] = useState('MyLogin');
    const [isAuthenticated, setIsAuthenticated] = useState(false);

    const removeNote = itemId => {
        deleteNoteById(itemId).then()
        const newOrder = notes.filter(el => el.id !== itemId);
        setNotes(newOrder);
    };

    useEffect(() => {
        if (!isAuthenticated) {
            loginUser(login).then((response) => {
                if (response.ok) {
                    response.json().then(() => {
                        setIsAuthenticated(true);
                        localStorage.setItem('clientUserId','10000000-0000-0000-0000-000000000001')
                    })
                } else {
                    registrationUser({
                        id: clientId,
                        userLogin: login
                    }).then((response) => {
                        if (response.ok) {
                            setIsAuthenticated(true);
                            localStorage.setItem('clientUserId','10000000-0000-0000-0000-000000000001')
                        }
                    })
                }
            });
        }
    }, [clientId]);

    useEffect(() => {
        getAllMyNotes(clientId)
            .then((response) => {
                if (response.ok) {
                    response.json().then((data) => setNotes(data));
                }
            });
    }, [clientId]);

    return (
        <>
            <NoteList notes={notes} removeNote={removeNote}/>
        </>
    );
}

export {Home};
