import {API_URL} from "./config";

const getNoteById = async (id) => {
    const response = await fetch(API_URL + 'note/find-one/' + id);
    return await response.json()
}

const getAllMyNotes = async (clientUserId) => {
    return await fetch(API_URL + 'client-user-note/find-all-my-notes/' + clientUserId);
}

const createNote = async (data) => {
    await fetch(API_URL + 'client-user-note/create', {
        method: 'post',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({
            "clientUserId": data.id,
            "text": data.text
        })
    });
}

const deleteNoteById = async (id) => {
    await fetch(API_URL + 'note/delete/' + id, {
        method: 'delete',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({
            "id": id
        })
    });
}

const updateNote = async (data) => {
    await fetch(API_URL + 'client-user-note/update', {
        method: 'put',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({
            "clientUserId": data.clientId,
            "noteId": data.noteId,
            "text": data.text
        })
    });
}

const loginUser = async (login) => {
    return await fetch(API_URL + 'auth/login', {
        method: 'post',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({
            "login": login
        })
    });
}

const registrationUser = async (data) => {
    console.log(data)
    return await fetch(API_URL + 'client-user/create', {
        method: 'post',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({
            "id": data.id,
            "login": data.userLogin
        })
    });
}


export {getNoteById, getAllMyNotes, createNote, deleteNoteById, updateNote, loginUser, registrationUser}