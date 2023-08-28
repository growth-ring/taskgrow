import { useState } from 'react';
import styled from 'styled-components';
import { SlArrowLeft, SlArrowRight, SlPlus } from "react-icons/sl";
import { useTodosStore } from '../../store/todos';
import AddTodos from './AddTodos';

const Container = styled.div`
    padding: 0 40px;
    height: 64px;
    display: flex;
    justify-content: space-between;
`;

const Arrow = styled.button`
    margin: 0 100px;
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
    const { today } = useTodosStore();
    const [years, month, day] = today.split("-");
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
                <Arrow><SlArrowLeft /></Arrow>
                <div>{`${years}년 ${month}월 ${day}일`}</div>
                <Arrow><SlArrowRight /></Arrow>
            </Date>
            <AddTodo onClick={handleShowAddTodos}><SlPlus /></AddTodo>
        </Container>
        {showAddTodos && <AddTodos getShowAddTodos={getShowAddTodos}/>}
        </>
    );
};

export default Header;