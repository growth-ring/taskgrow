import { useState } from 'react';
import styled from 'styled-components';
import { SlPlus } from "react-icons/sl";
import AddTodos from './AddTodos';
import HeaderDate from './HeaderDate';

const Container = styled.div`
    padding: 0 40px;
    height: 64px;
    display: flex;
    justify-content: space-between;
`;

const Date = styled.div`
    display: flex;
    align-items: center;
    font-size: 22px;
`;

const AddTodo = styled.button`
    font-size: 25px;
    margin-right: 100px;
`;

const Header = () => {
    const [showAddTodos, setShowAddTodos] = useState(false);

    const handleShowAddTodos = () => {
        setShowAddTodos(true);
    }

    const getShowAddTodos = (todos: boolean) => {
        setShowAddTodos(todos);
    };

    return (
        <>
        <Container>
            <Date>
                <HeaderDate />
            </Date>
            <AddTodo onClick={handleShowAddTodos}><SlPlus /></AddTodo>
        </Container>
        {showAddTodos && <AddTodos getShowAddTodos={getShowAddTodos}/>}
        </>
    );
};

export default Header;
