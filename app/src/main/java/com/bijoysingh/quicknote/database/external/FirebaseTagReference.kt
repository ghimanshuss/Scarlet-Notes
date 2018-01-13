package com.bijoysingh.quicknote.database.external

import android.content.Context
import com.bijoysingh.quicknote.MaterialNotes
import com.bijoysingh.quicknote.database.Tag
import com.bijoysingh.quicknote.utils.genFromFirebase
import com.github.bijoysingh.starter.util.TextUtils
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase


/**
 * Functions for Database Reference for Firebase Notes
 */

fun tagDatabaseReference(context: Context, userId: String) {
  if (MaterialNotes.firebaseTag !== null) {
    return
  }
  MaterialNotes.firebaseTag = FirebaseDatabase
      .getInstance()
      .getReference()
      .child("tags")
      .child(userId)
  setListener(context)
}

fun removeTagDatabaseReference() {
  MaterialNotes.firebaseTag = null
}

fun insertTagToFirebase(note: FirebaseTag) {
  if (MaterialNotes.firebaseTag == null) {
    return
  }
  MaterialNotes.firebaseTag!!.child(note.uuid).setValue(note)
}

fun deleteTagFromFirebase(note: FirebaseTag) {
  if (MaterialNotes.firebaseTag == null) {
    return
  }
  MaterialNotes.firebaseTag!!.child(note.uuid).removeValue()
}

private fun setListener(context: Context) {
  if (MaterialNotes.firebaseTag === null) {
    return
  }
  MaterialNotes.firebaseTag!!.addChildEventListener(object : ChildEventListener {
    override fun onCancelled(p0: DatabaseError?) {
      // Ignore cancelled
    }

    override fun onChildMoved(snapshot: DataSnapshot?, p1: String?) {
      // Ignore moved child
    }

    override fun onChildChanged(snapshot: DataSnapshot?, p1: String?) {
      onChildAdded(snapshot, p1)
    }

    override fun onChildAdded(snapshot: DataSnapshot?, p1: String?) {
      handleTagChange(snapshot, fun(tag, existingTag, isSame) {
        if (existingTag === null) {
          tag.saveWithoutSync(context)
          return
        }
        if (!isSame) {
          existingTag.title = tag.title
          existingTag.saveWithoutSync(context)
        }
      })
    }

    override fun onChildRemoved(snapshot: DataSnapshot?) {
      handleTagChange(snapshot, fun(_, existingTag, _) {
        if (existingTag !== null) {
          existingTag.deleteWithoutSync(context)
        }
      })
    }

    fun handleTagChange(
        snapshot: DataSnapshot?,
        listener: (Tag, Tag?, Boolean) -> Unit) {
      if (snapshot === null) {
        return
      }
      try {
        val tag = snapshot.getValue(FirebaseTag::class.java)
        if (tag === null) {
          return
        }

        val notifiedTag = genFromFirebase(tag)
        val existingTag = Tag.db(context).getByUUID(tag.uuid)
        var isSame = false
        if (existingTag !== null) {
          isSame = TextUtils.areEqualNullIsEmpty(notifiedTag.title, existingTag.title)
        }

        listener(notifiedTag, existingTag, isSame)
      } catch (e: Exception) {
        // Ignore if exception
      }
    }
  })
}