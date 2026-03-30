package com.velsol.core.domain.di

/**
 * Top-level scope marker for the application's single dependency graph.
 * Client modules contribute bindings to this scope via @ContributesBinding(AppScope::class).
 */
object AppScope
