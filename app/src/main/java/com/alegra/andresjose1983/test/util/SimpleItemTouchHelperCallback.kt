package com.alegra.andresjose1983.test.util

/**
 * Created by andre on 3/14/2018.
 */

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.support.v4.content.ContextCompat
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import com.alegra.andresjose1983.test.R
import com.alegra.andresjose1983.test.adapter.ContactAdapter

class SimpleItemTouchHelperCallback(private val context: Context,
                                    private val adapter: ContactAdapter) : ItemTouchHelper.Callback() {
    override fun onMove(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?, target: RecyclerView.ViewHolder?): Boolean {
        return true
    }

    private val paint = Paint()

    override fun isLongPressDragEnabled(): Boolean {
        return true
    }

    override fun isItemViewSwipeEnabled(): Boolean {
        return true
    }

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        // Set movement flags based on the layout manager
        if (recyclerView.layoutManager is GridLayoutManager) {
            val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            val swipeFlags = 0
            return ItemTouchHelper.Callback.makeMovementFlags(dragFlags, swipeFlags)
        } else {
            val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
            val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END
            return ItemTouchHelper.Callback.makeMovementFlags(dragFlags, swipeFlags)
        }
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        //if (direction == ItemTouchHelper.RIGHT || direction == ItemTouchHelper.LEFT)
        adapter.onItemDismiss(viewHolder.adapterPosition)
    }

    override fun onChildDraw(c: Canvas, recyclerView: RecyclerView,
                             viewHolder: RecyclerView.ViewHolder, dX: Float,
                             dY: Float, actionState: Int, isCurrentlyActive: Boolean) {

        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

            val itemView = viewHolder.itemView
            val height = itemView.bottom.toFloat() - itemView.top.toFloat()
            val width = height / 3
            paint.color = ContextCompat.getColor(context, R.color.colorPrimaryDark)
            if (dX > 0) {
                val background = RectF(itemView.left.toFloat(),
                        itemView.top.toFloat(), dX, itemView.bottom.toFloat())
                c.drawRect(background, paint)
                val iconDest = RectF(itemView.left.toFloat() + width,
                        itemView.top.toFloat() + width,
                        itemView.left.toFloat() + 2 * width,
                        itemView.bottom.toFloat() - width)

                c.drawBitmap(getBitmapFromVectorDrawable(recyclerView.context,
                        R.drawable.ic_delete), null, iconDest, paint)
            } else if (dX < 0) {
                val background = RectF(itemView.right.toFloat() + dX,
                        itemView.top.toFloat(), itemView.right.toFloat(),
                        itemView.bottom.toFloat())
                c.drawRect(background, paint)
                val iconDest = RectF(itemView.right.toFloat() - 2 * width,
                        itemView.top.toFloat() + width,
                        itemView.right.toFloat() - width,
                        itemView.bottom.toFloat() - width)
                c.drawBitmap(getBitmapFromVectorDrawable(recyclerView.context,
                        R.drawable.ic_delete), null, iconDest, paint)
            }
        }
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState,
                isCurrentlyActive)
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        // We only want the active item to change
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            if (viewHolder is ItemTouchHelperViewHolder) {
                // Let the view holder know that this item is being moved or dragged
                val itemViewHolder = viewHolder as ItemTouchHelperViewHolder?
                itemViewHolder!!.onItemSelected()
            }
        }

        super.onSelectedChanged(viewHolder, actionState)
    }

    override fun clearView(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)

        viewHolder.itemView.alpha = 1.0f

        if (viewHolder is ItemTouchHelperViewHolder) {
            // Tell the view holder it's time to restore the idle state
            val itemViewHolder = viewHolder as ItemTouchHelperViewHolder
            itemViewHolder.onItemClear()
        }
    }

    private fun getBitmapFromVectorDrawable(context: Context, drawableId: Int): Bitmap {
        var drawable = ContextCompat.getDrawable(context, drawableId)

        val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth,
                drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)

        return bitmap
    }
}
