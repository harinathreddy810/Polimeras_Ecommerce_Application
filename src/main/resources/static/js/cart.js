// const cartBadge = document.getElementById('cartBadge');
// const withCreds = { credentials: 'include' };
// const toast = msg => alert(msg);
//
// /* ----- POST /add-to-cart -------------------------------------------- */
// export async function addToCart(productId, qty = 1) {
//     try {
//         const r = await fetch(`/api/user/add-to-cart/${productId}?quantity=${qty}`, {
//             method: 'POST',
//             ...withCreds
//         });
//         if (!r.ok) throw new Error(await r.text());
//
//         await refreshCartCount();
//         toast('Added to cart ✔');
//     } catch (e) {
//         console.error(e);
//         if (e.message.includes('Unauthorized') || e.message.includes('Forbidden'))
//             location.href = '/login.html';
//         else
//             toast(e.message || 'Failed to add to cart');
//     }
// }
//
// /* ----- GET /cart ----------------------------------------------------- */
// export async function refreshCartCount() {
//     if (!cartBadge) return;
//     try {
//         const r = await fetch('/api/user/cart', withCreds);
//         if (!r.ok) throw new Error();
//         const { cartItems = [] } = await r.json();
//         const total = cartItems.reduce((s, it) => s + it.quantity, 0);
//
//         cartBadge.textContent = total;
//         cartBadge.classList.toggle('d-none', total ===   0);
//     } catch {
//         cartBadge.classList.add('d-none');
//     }
// }
//
// /* ----- Optional: Render Cart Items ---------------------------------- */
// export async function renderCartItems() {
//     try {
//         const r = await fetch('/api/user/cart', withCreds);
//         if (!r.ok) throw new Error('Failed to fetch cart items');
//         const { cartItems = [] } = await r.json();
//
//         const container = document.getElementById('cartItemsContainer');
//         container.innerHTML = ''; // Clear old items
//
//         cartItems.forEach(item => {
//             const product = item.product;
//             console.log(product)
//             const div = document.createElement('div');
//             div.className = 'cart-item';
//             div.innerHTML = `
//                 <img src="/images/${product.imageName}" alt="${product.name}" width="50" />
//                 <div>
//                     <h5>${product.name}</h5>
//                     <p>Price: ₹${product.price}</p>
//                     <p>Quantity: ${item.quantity}</p>
//                 </div>
//             `;
//             container.appendChild(div);
//         });
//     } catch (e) {
//         console.error(e);
//         toast('Error loading cart');
//     }
// }
//
//
// /* run once per page */
// window.addEventListener('DOMContentLoaded', () => {
//     refreshCartCount();
// });


//
// const cartBadge = document.getElementById('cartBadge');
// const withCreds = { credentials: 'include' };
//
// /* ----- POST /add-to-cart -------------------------------------------- */
// export async function addToCart(productId, qty = 1) {
//     try {
//         const r = await fetch(`/api/user/add-to-cart/${productId}?quantity=${qty}`, {
//             method: 'POST',
//             ...withCreds
//         });
//         if (!r.ok) throw new Error(await r.text());
//         await refreshCartCount();
//         // No alert or popup
//     } catch (e) {
//         console.error(e);
//         if (e.message.includes('Unauthorized') || e.message.includes('Forbidden')) {
//             location.href = '/login.html';
//         }
//     }
// }
//
// /* ----- GET /cart ----------------------------------------------------- */
// export async function refreshCartCount() {
//     if (!cartBadge) return;
//     try {
//         const r = await fetch('/api/user/cart', withCreds);
//         if (!r.ok) throw new Error();
//         const { cartItems = [] } = await r.json();
//         const total = cartItems.reduce((s, it) => s + it.quantity, 0);
//         cartBadge.textContent = total;
//         cartBadge.classList.toggle('d-none', total === 0);
//     } catch {
//         cartBadge.classList.add('d-none');
//     }
// }
//
// /* ----- Optional: Render Cart Items ---------------------------------- */
// export async function renderCartItems() {
//     try {
//         const r = await fetch('/api/user/cart', withCreds);
//         if (!r.ok) throw new Error('Failed to fetch cart items');
//         const { cartItems = [] } = await r.json();
//
//         const container = document.getElementById('cartItemsContainer');
//         container.innerHTML = ''; // Clear old items
//
//         cartItems.forEach(item => {
//             const product = item.product;
//             const div = document.createElement('div');
//             div.className = 'cart-item';
//             div.innerHTML = `
//                 <div style="display: flex; align-items: center; gap: 1rem; margin-bottom: 1rem; border: 1px solid #ddd; padding: 10px; border-radius: 5px;">
//                     <img src="/images/${product.imageName}" alt="${product.name}" width="50" />
//                     <div style="flex: 1;">
//                         <h5 style="margin: 0 0 5px;">${product.name}</h5>
//                         <p style="margin: 0;">Price: ₹${product.price}</p>
//                         <p style="margin: 0;">Quantity in cart: ${item.quantity}</p>
//                     </div>
//                     <div>
//                         <input type="number" min="1" value="1" id="qty-${product.id}" style="width: 60px;" />
//                         <button data-id="${product.id}" class="add-more-btn">Add</button>
//                     </div>
//                 </div>
//             `;
//             container.appendChild(div);
//         });
//
//         // Attach add buttons
//         container.querySelectorAll('.add-more-btn').forEach(button => {
//             button.addEventListener('click', async () => {
//                 const id = button.dataset.id;
//                 const qtyInput = document.getElementById(`qty-${id}`);
//                 const qty = parseInt(qtyInput.value);
//                 if (qty > 0) {
//                     await addToCart(id, qty);
//                 }
//             });
//         });
//
//     } catch (e) {
//         console.error(e);
//     }
// }
//
// /* ----- On Page Load -------------------------------------------------- */
// window.addEventListener('DOMContentLoaded', () => {
//     refreshCartCount();
// });



const cartBadge = document.getElementById('cartBadge');
const withCreds = { credentials: 'include' };

/* ----- POST /add-to-cart -------------------------------------------- */
export async function addToCart(productId, qty = 1) {
    try {
        const r = await fetch(`/api/user/add-to-cart/${productId}?quantity=${qty}`, {
            method: 'POST',
            ...withCreds
        });
        if (!r.ok) throw new Error(await r.text());

        await renderCartItems(); // Refresh items
        await refreshCartCount(); // Refresh badge
    } catch (e) {
        console.error(e);
        if (e.message.includes('Unauthorized') || e.message.includes('Forbidden')) {
            location.href = '/login.html';
        }
    }
}

/* ----- DELETE /cart/decrease/{productId} ----------------------------- */
export async function removeOneFromCart(productId) {
    try {
        const r = await fetch(`/api/user/cart/decrease/${productId}`, {
            method: 'DELETE',
            ...withCreds
        });
        if (!r.ok) throw new Error(await r.text());

        await renderCartItems(); // Refresh items
        await refreshCartCount(); // Refresh badge
    } catch (e) {
        console.error(e);
    }
}

/* ----- GET /cart ----------------------------------------------------- */
export async function refreshCartCount() {
    if (!cartBadge) return;
    try {
        const r = await fetch('/api/user/cart', withCreds);
        if (!r.ok) throw new Error();
        const { cartItems = [] } = await r.json();
        const total = cartItems.reduce((sum, item) => sum + item.quantity, 0);

        cartBadge.textContent = total;
        cartBadge.classList.toggle('d-none', total === 0);
    } catch {
        cartBadge.classList.add('d-none');
    }
}

/* ----- RENDER CART ITEMS --------------------------------------------- */
export async function renderCartItems() {
    try {
        const r = await fetch('/api/user/cart', withCreds);
        if (!r.ok) throw new Error('Failed to fetch cart items');

        const { cartItems = [] } = await r.json();
        const container = document.getElementById('cartItemsContainer');
        container.innerHTML = ''; // Clear old items

        cartItems.forEach(item => {
            const product = item.product;
            const div = document.createElement('div');
            div.className = 'cart-item';
            div.innerHTML = `
                <div style="display: flex; align-items: center; gap: 1rem; margin-bottom: 1rem; border: 1px solid #ddd; padding: 10px; border-radius: 5px;">
                    <img src="/images/${product.imageName}" alt="${product.name}" width="50" />
                    <div style="flex: 1;">
                        <h5 style="margin: 0 0 5px;">${product.name}</h5>
                        <p style="margin: 0;">Price: ₹${product.price}</p>
                        <p style="margin: 0;">Price: ₹${product.quantityType}</p>
<!--                        <p style="margin: 0;">Quantity in cart: ${product.stockQuantity}</p>-->
                        <p style="margin: 0;">
                        ${product.stockQuantity === 0 ? 'Out of Stock' : `Quantity in cart: ${product.stockQuantity}`}
                        </p>
                    </div>
                    <div>
                        <input type="number" min="1" value="1" id="qty-${product.id}" style="width: 60px;" />
                        <button data-id="${product.id}" class="add-more-btn">+</button>
                        <button data-id="${product.id}" class="remove-one-btn">−</button>
                    </div>
                </div>
            `;
            container.appendChild(div);
        });

        // Add event listeners for + buttons
        container.querySelectorAll('.add-more-btn').forEach(button => {
            button.addEventListener('click', async () => {
                const id = button.dataset.id;
                const qtyInput = document.getElementById(`qty-${id}`);
                const qty = parseInt(qtyInput.value);
                if (qty > 0) {
                    await addToCart(id, qty);
                }
            });
        });

        // Add event listeners for − buttons
        container.querySelectorAll('.remove-one-btn').forEach(button => {
            button.addEventListener('click', async () => {
                const id = button.dataset.id;
                await removeOneFromCart(id);
            });
        });

    } catch (e) {
        console.error(e);
    }
}

/* ----- On Page Load -------------------------------------------------- */
window.addEventListener('DOMContentLoaded', () => {
    refreshCartCount();
    renderCartItems(); // Also load cart items directly
});
